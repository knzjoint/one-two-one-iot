package cn.makerknz.product.server.service;

import cn.makerknz.product.server.domain.form.UserRegisterForm;
import cn.makerknz.product.server.domain.vo.UserInfoVO;
import cn.makerknz.product.server.domain.enums.SmsTypeEnum;

/**
 * @Author: 朱康南
 * @Date: 2019/9/4/004 17:58
 * @Version 1.0
 */

public interface IUserOperatorService {

    /**
     * 注册发送手机验证码
     * @param phoneNo
     */
    void registerSmsSend(String phoneNo);

    /**
     * 修改密码发送手机验证码
     * @param phoneNo
     */
    void changePasswordSmsSend(String phoneNo);

    /**
     * 第三方绑定时注册或绑定用户的验证码
     * @param phoneNo
     */
    void registerOrBindSmsSend(String phoneNo);

    /**
     * 注册
     */
    UserInfoVO register(UserRegisterForm registerForm, SmsTypeEnum type);

    /**
     * 修改用户密码
     * @param registerForm
     * @return
     */
    UserInfoVO changeUserPassword(UserRegisterForm registerForm);
}
