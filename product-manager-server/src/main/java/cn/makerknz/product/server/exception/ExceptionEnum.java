package cn.makerknz.product.server.exception;

import lombok.Getter;

/**
 * @Author: maker_knz
 * @Date: 2021/4/19/019 14:32
 * @Version 1.0
 */

@Getter
public enum ExceptionEnum {
//    ERROR(-1, "服务端错误"),
    SUCCESS(2000, "成功"),
//    TOKEN_ERROR(1009, "token无效"),

    //400系列
    BAD_REQUEST(400,"请求的数据格式不符!"),
    UNAUTHORIZED(401,"登录凭证过期!"),
    APPLICATION_UNAUTHORIZED(401,"登录凭证过期!"),
    FORBIDDEN(403,"抱歉，你无权限访问!"),
    NOT_FOUND(404, "请求的资源找不到!"),

    //500系列
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVICE_UNAVAILABLE(503,"服务器正忙，请稍后再试!"),

    //未知异常
    UNKNOWN(10000,"未知异常!"),

    //自定义系列
    USER_NAME_IS_NOT_NULL(10001,"【参数校验】用户名不能为空"),
    PWD_IS_NOT_NULL(10002,"【参数校验】密码不能为空"),
    ADD_USER_ERROR(10003, "创建用户错误"),
    UPDATE_USER_ERROR(10004, "更新用户错误"),
    WECHAT_LOGIN_ERROR(10005, "校验失败"),

    // 应用端
    APPLICATION_WEBHOOK_AUTHORIZED_ERROR(11001, "没有权限使用WebHook!"),
    APPLICATION_WEBHOOK_SETTING_ERROR(11002, "WebHook配置错误!"),
    APPLICATION_DELETED_ERROR(11003, "删除应用失败!"),

    // 用户
    USER_REGISTER_ERROR(12001, "用户注册错误!"),
    USER_PERMISSION_ERROR(12002, "用户权限错误!"),
    USER_DELETED_ERROR(12003, "删除用户错误!"),
    USER_REGISTER_PHONE_ERROR(12004, "注册手机号错误!"),
    USER_REGISTER_PHONE_EXISTED(12005, "注册手机号已经注册!"),
    USER_REGISTER_PHONE_CODE_ERROR(12005, "注册手机号验证码错误!"),
    USER_PASSWORD_NOT_EMPTY(12005, "密码不能为空!"),
    USER_ALIYUN_SMS_ERROR(12005, "阿里云SMS验证码错误!"),
    USER_LOGIN_PHONE_PASSWORD_ERROR(12005, "用户登录账户密码错误!"),

    // 产品
    PRODUCT_ADD_TOPIC_ERROR(13001, "添加主题错误!"),
    PRODUCT_TOPIC_NOTFOUND(13002, "要删除的主题不存在!"),
    PRODUCT_TOPIC_DELETED_ERROR(13003, "删除主题失败!"),
    PRODUCT_TOPIC_UPDATED_ERROR(13004, "更新主题失败!"),
    PRODUCT_TOPIC_EXISTED_ERROR(13005, "主题已经存在!"),
    PRODUCT_NOTFOUND(13006, "没有查找到产品!"),
    PRODUCT_ADD_ERROR(13007, "添加产品错误!"),

    // MQTT
    MQTT_ADD_USER_ERROR(14001, "添加Mqtt用户失败!"),
    MQTT_DELETED_USER_ERROR(14002, "删除设备账号失败!"),

    // Device
    DEVICE_EXISTED_ERROR(15002, "设备名已经存在!"),

    // Things 物
    THINGS_ADD_ERROR(16001, "添加物模型错误"),
    THINGS_USER_NO_PERMISSION(16002, "没有权限访问物"),

    // MINIO
    MINIO_IMAGE_UPLOAD_ERROR(17001, "上传图片错误！"),


    // Websocket
    WEBSOCKET_HANDSHAKE_ERROR(18001, "Websocket 连接错误！"),
    WEBSOCKET_IO_ERROR(18002, "websocket IO异常!"),
    WEBSOCKET_USER_NO_LOGIN(18003, "用户没有上线！"),

    // Venues
    VENUES_ADD_ERROR(19001, "场地/房间添加错误！"),
    VENUES_THINGS_PERMISSION_ERROR(19001, "场地/房间权限错误！"),
    VENUES_THINGS_NOT_FOUND(19003, "场地/房间没找到物模型!"),


    // 场景联动
    THINGS_LINKAGE_PERMISSION_ERROR(20001, "场景联动没有权限!"),

    // Rule
    RULE_PARSER_ERROR(21001, "联动解析错误!"),
    RULE_NOT_FOUND_END_NODE(21002, "不存在执行节点!"),
    ;

    Integer code;

    String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
