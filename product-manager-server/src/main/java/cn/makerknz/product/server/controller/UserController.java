package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckAuthorization;
import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.docApi.UserControllerApi;
import cn.makerknz.product.server.domain.dto.JwtTokenRespDTO;
import cn.makerknz.product.server.domain.dto.LoginRespDTO;
import cn.makerknz.product.server.domain.dto.UserLoginDTO;
import cn.makerknz.product.server.domain.dto.UserRespDTO;
import cn.makerknz.product.server.domain.form.UserForm;
import cn.makerknz.product.server.domain.form.UserRegisterCodeForm;
import cn.makerknz.product.server.domain.form.UserRegisterForm;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.domain.vo.UserInfoVO;
import cn.makerknz.product.server.domain.vo.UserVO;
import cn.makerknz.product.server.entity.User;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.domain.enums.SmsTypeEnum;
import cn.makerknz.product.server.service.IUserOperatorService;
import cn.makerknz.product.server.service.IUserService;
import cn.makerknz.product.server.utils.BCrypt;
import cn.makerknz.product.server.utils.JwtTokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController implements UserControllerApi {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserOperatorService userOperatorService;

    @PostMapping("/login")
    public ResultVO login(@RequestBody UserLoginDTO userLoginDTO) {

        User user = new User();
        user.setPhone(userLoginDTO.getPhone());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User one = userService.getOne(queryWrapper);
        if (one == null) {
            throw new BusinessException(ExceptionEnum.USER_LOGIN_PHONE_PASSWORD_ERROR);
        }
        boolean checkpw = BCrypt.checkpw(userLoginDTO.getPassword(), one.getPassword());
        if (!checkpw) {
            throw new BusinessException(ExceptionEnum.USER_LOGIN_PHONE_PASSWORD_ERROR);
        }

        // 发放token
        return ResultVO.success(this.genToken(one));
    }

    @GetMapping("/logout")
    public ResultVO logout() {
        return ResultVO.success();
    }

    @GetMapping("/user-info")
    @CheckLogin
    public ResultVO<UserInfoVO> userInfo(HttpServletRequest httpServletRequest) {
        User user = User.builder()
                .id((Integer) httpServletRequest.getAttribute("id"))
                .build();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User one = userService.getOne(queryWrapper);

        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(one, userInfoVO);

        return ResultVO.success(userInfoVO);
    }

    @GetMapping(path = "/{id}")
    @CheckLogin
    public User getUser(@PathVariable("id") Integer userId) {
        User user = userService.getById(userId);
        UserVO userVO = new UserVO();
        return user;
    }

    @PutMapping("/{id}")
    @CheckAuthorization("admin")
    public ResultVO updateUser(@PathVariable("id") Integer id, @RequestBody UserForm userForm) {
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        user.setId(id);
        boolean bUpdateUser = userService.updateById(user);
        if (!bUpdateUser) {
            return ResultVO.error(ExceptionEnum.UPDATE_USER_ERROR);
        }

        return ResultVO.successMsg("修改用户成功");
    }

    @PostMapping("/add-user")
//    @CheckAuthorization("admin")
    public ResultVO addUser(@RequestBody UserForm userForm) {
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        String hashpw = BCrypt.hashpw(userForm.getPassword(), BCrypt.gensalt());
        user.setPassword(hashpw);

        boolean save = userService.save(user);
        if (!save) {
            return ResultVO.error(ExceptionEnum.ADD_USER_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User one = userService.getOne(queryWrapper);

        return ResultVO.successMsg("添加用户成功");
    }

    @DeleteMapping("/delete-user/{id}")
    @CheckAuthorization("admin")
    public ResultVO deleteUser(@PathVariable("id") Integer id) {
        boolean isDelete = this.userService.removeById(id);
        if (!isDelete) {
            throw new RuntimeException();
        }
        return ResultVO.success();
    }

    @GetMapping("/watiers")
    @CheckAuthorization("admin")
    public ResultVO getWatiers() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("role", 2);
        List<User> userList = this.userService.list(userQueryWrapper);
        return ResultVO.success(userList);
    }

    @GetMapping("/gen-token")
    @CheckAuthorization("admin")
    public String genToken() {
        // 发放token
        Map<String, Object> userInfo = new HashMap<>(3);
        userInfo.put("id", 1);
        userInfo.put("nickname", "knz");
        userInfo.put("role", 0);
        String token = JwtTokenUtils.generateToken(userInfo);
        log.info("用户生成的token: {}", token);

        return token;
    }

    @GetMapping("/test")
    @CheckLogin
    public String test() {
        // 发放token
        log.info("成功获取");
        return "";
    }

    @PostMapping("/register")
    public ResultVO registerUser(@RequestBody @Valid UserRegisterForm userForm, BindingResult result) {
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                throw new BusinessException(ExceptionEnum.USER_REGISTER_ERROR);
            }
        }

        UserInfoVO register = this.userOperatorService.register(userForm, SmsTypeEnum.SMS_REGISTER_USER);

        return ResultVO.success(register);
    }

    @PostMapping("/change-password")
    public ResultVO changePassword(@RequestBody UserRegisterForm userForm) {

        UserInfoVO register = this.userOperatorService.changeUserPassword(userForm);

        return ResultVO.success(register);
    }

    @PostMapping("/register/code")
    public ResultVO registerCode(@RequestBody UserRegisterCodeForm userCodeForm) {
        userOperatorService.registerSmsSend(userCodeForm.getPhone());
        return ResultVO.successMsg("发送验证码成功");
    }

    @PostMapping("/change-password/code")
    public ResultVO changePasswordCode(@RequestBody UserRegisterForm userForm) {
        userOperatorService.changeUserPassword(userForm);
        return ResultVO.successMsg("发送验证码成功");
    }


    private LoginRespDTO genToken(User user) {
        // 发放token
        Map<String, Object> userInfo = new HashMap<>(3);
        userInfo.put("id", user.getId());
        userInfo.put("nickname", user.getUsername());
        userInfo.put("role", user.getRole());
        String token = JwtTokenUtils.generateToken(userInfo);
        log.info("用户生成的token: {}", token);
        LoginRespDTO loginRespDTO = LoginRespDTO.builder()
                .user(UserRespDTO.builder().id(user.getId()).wxNickname(user.getUsername()).build())
                .token(JwtTokenRespDTO.builder().accessToken(token).expirationTime(JwtTokenUtils.getExpirationTime().getTime()).build())
                .build();

        return loginRespDTO;
    }

}
