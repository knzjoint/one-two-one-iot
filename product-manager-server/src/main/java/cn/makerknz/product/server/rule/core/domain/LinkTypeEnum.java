package cn.makerknz.product.server.rule.core.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 12:21
 */

@Getter
public enum LinkTypeEnum {

    THINGS_TYPE(0, "物模型"),
    ;

    @EnumValue //标记数据库存的值是code
    private final int code;

    String desc;

    LinkTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
