package cn.makerknz.product.server.domain.enums;

import lombok.Getter;

/**
 * @Author: 朱康南
 * @Date: 2019/12/28/028 15:03
 * @Version 1.0
 */

@Getter
public enum SmsTypeEnum {

    SMS_REGISTER_USER(0, "REGISTER_USER", "SMS_173340575", "注册用户"),

    SMS_VALIDATE_USER(1, "VALIDATE_USER", "SMS_124795078", "验证用户"),

    SMS_CHANGE_PASSWORD(2, "CHANGE_PASSWORD", "SMS_124795079", "修改用户密码"),

    SMS_LOGIN_USER(3, "LOGIN_USER", "SMS_181720061", "用户登录"),

    SMS_SOCIAL_REGISTER_BIND(4, "SOCIAL_REGISTER_BIND", "SMS_124795079", "用户微信绑定"),

    ;

    private Integer code;

    private String value;

    /**
     * 短信消息的模板
     */
    private String template;

    private String desc;

    SmsTypeEnum(Integer code, String value, String template, String desc) {
        this.code = code;
        this.value = value;
        this.template = template;
        this.desc = desc;
    }

}
