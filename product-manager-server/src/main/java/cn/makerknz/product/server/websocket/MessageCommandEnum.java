package cn.makerknz.product.server.websocket;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * Websocket 通讯时作为消息类别的标识
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/25 17:55
 */
public enum MessageCommandEnum {

    THINGS_CHANGED(0, "物值变动！"),
    ;

    @EnumValue //标记数据库存的值是code
    private final int code;

    String desc;

    MessageCommandEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
