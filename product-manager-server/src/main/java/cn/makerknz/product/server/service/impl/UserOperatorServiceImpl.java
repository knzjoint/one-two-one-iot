package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.domain.enums.RoleEnum;
import cn.makerknz.product.server.domain.enums.SmsTypeEnum;
import cn.makerknz.product.server.domain.form.UserRegisterForm;
import cn.makerknz.product.server.domain.vo.UserInfoVO;
import cn.makerknz.product.server.entity.User;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.service.IUserOperatorService;
import cn.makerknz.product.server.service.IUserService;
import cn.makerknz.product.server.utils.BCrypt;
import cn.makerknz.product.server.utils.SmsUtils;
import cn.makerknz.product.server.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: maker_knz
 * @Date: 2021/10/28/028 16:41
 * @Version 1.0
 */

@Service
@Slf4j
public class UserOperatorServiceImpl implements IUserOperatorService {

    @Autowired
    private IUserService userService;

//    @Autowired
//    private IMqttUserService mqttUserService;

    @Autowired
    private SmsUtils smsUtils;

    /**
     * 发送手机验证码
     *
     * @param phoneNo
     */
    @Override
    public void registerSmsSend(String phoneNo) {
        // 1.检测手机号是否已经注册
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", phoneNo);
        User userServiceOne = userService.getOne(userQueryWrapper);
        if (userServiceOne != null) {
            log.warn("[注册发送验证码],手机号错误，手机号{}已经注册", phoneNo);
            throw new BusinessException(ExceptionEnum.USER_REGISTER_PHONE_ERROR);
        }

        this.smsSend(phoneNo, SmsTypeEnum.SMS_REGISTER_USER);
    }

    /**
     * 修改密码发送手机验证码
     *
     * @param phoneNo
     */
    @Override
    public void changePasswordSmsSend(String phoneNo) {
        this.smsSend(phoneNo, SmsTypeEnum.SMS_CHANGE_PASSWORD);
    }

    /**
     * 第三方绑定时注册或绑定用户的验证码
     *
     * @param phoneNo
     */
    @Override
    public void registerOrBindSmsSend(String phoneNo) {
        this.smsSend(phoneNo, SmsTypeEnum.SMS_SOCIAL_REGISTER_BIND);
    }

    /**
     * 注册
     *
     * @param registerForm
     */
    @Override
    public UserInfoVO register(UserRegisterForm registerForm, SmsTypeEnum type) {
        // 2.判断用户名&&手机号是否重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", registerForm.getPhone());
        User userServiceOne = userService.getOne(userQueryWrapper);
        if (userServiceOne != null) {
            log.warn("[注册],用户名已经存在，用户名:{}", registerForm.getPhone());
            throw new BusinessException(ExceptionEnum.USER_REGISTER_PHONE_EXISTED);
        }

        // 3.判断验证码是否有效
        boolean validateCode = smsUtils.validateCode(registerForm.getPhone(), registerForm.getCode(), type);
        if (!validateCode) {
            throw new BusinessException(ExceptionEnum.USER_REGISTER_PHONE_CODE_ERROR);
        }
        // 4.生成nickname
        String nickname = UserUtils.autoCreateNickname();

        // 4.将注册内容保存到数据库
        User newUser = new User();
        newUser.setPhone(registerForm.getPhone());
        newUser.setAvatar("http://onetwoone.makerknz.cn:9000/image/10723-1F52214231E12.jpg");
        newUser.setPassword(BCrypt.hashpw(registerForm.getPassword(), BCrypt.gensalt()));
        newUser.setUsername(nickname);
        newUser.setRole(RoleEnum.PERSONAL);

        // 7.保存
        boolean save = userService.save(newUser);
        if (!save) {
            throw new BusinessException(ExceptionEnum.USER_REGISTER_ERROR);
        }

        // 8、注册mqtt账户
//        String prePassword = DeviceUtils.getSalt();
//        String salt = DeviceUtils.getSalt();
//        String clientId = DeviceUtils.getSalt();
//        String clientName = DeviceUtils.getSalt();
//        String password = DeviceUtils.getSaltMD5(prePassword, salt);
//        MqttUser mqttUserForm = new MqttUser();
//        mqttUserForm.setClientId(clientId);
//        mqttUserForm.setPassword(password);
//        mqttUserForm.setUserId(newUser.getId());
//        mqttUserForm.setUsername(clientName);
//        boolean save1 = mqttUserService.save(mqttUserForm);
//        if (!save1) {
//            throw new RuntimeException("保持mqtt用户和用户关系错误");
//        }

        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(newUser.getId())
                .role(newUser.getRole())
                .truename(newUser.getTruename())
                .username(newUser.getUsername())
                .build();

        // 8.返回个人信息
        return userInfoVO;
    }

    /**
     * 修改用户密码
     *
     * @param registerForm
     * @return
     */
    @Override
    public UserInfoVO changeUserPassword(UserRegisterForm registerForm) {

        if (registerForm.getPassword().isEmpty()) {
            log.warn("[修改密码],密码不能为空，用户名:{}", registerForm.getPhone());
            throw new RuntimeException("密码不能为空");
        }

        // 2.能过phone读取用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", registerForm.getPhone());
        User userServiceOne = userService.getOne(userQueryWrapper);
        if (userServiceOne == null) {
            log.warn("[修改密码],用户不存在，用户名:{}", registerForm.getPhone());
            throw new RuntimeException("用户不存在");
        }

        // 3.判断验证码是否有效
        Boolean validateCode = smsUtils.validateCode(registerForm.getPhone(), registerForm.getCode(), SmsTypeEnum.SMS_CHANGE_PASSWORD);
        if (!validateCode) {
            log.error("[注册验证验证码], 验证验证码错误, 手机号 = {}, 验证码 = {}", registerForm.getPhone(), registerForm.getCode());
            throw new RuntimeException("验证验证码错误");
        }

        // 4.修改密码,加密存储
        userServiceOne.setPassword(BCrypt.hashpw(registerForm.getPassword(), BCrypt.gensalt()));

        // 7.保存
        boolean update = userService.updateById(userServiceOne);
        if (!update) {
            log.warn("[修改密码],更新密码失败，用户名:{}", registerForm.getPhone());
            throw new RuntimeException("更新密码失败");
        }

        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(userServiceOne.getId())
                .role(userServiceOne.getRole())
                .truename(userServiceOne.getTruename())
                .username(userServiceOne.getUsername())
                .build();

        return userInfoVO;
    }


    private void smsSend(String phoneNo, SmsTypeEnum smsType) {
        smsUtils.send(phoneNo, smsType);
    }

}
