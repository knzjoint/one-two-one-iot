package cn.makerknz.product.server.rule.core.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 12:21
 */

@Getter
public enum RelationalOperatorEnum {

    EMPTY(0, "==","相等"),
    EQ(0, "==","相等"),
    NE(1, "!=","不等于"),
    LT(2, "<", "小于"),
    GT(3, ">", "大于"),
    LE(4, "<=", "小于或等于"),
    GE(5, ">=", "大于或等于"),
    ;

    @EnumValue //标记数据库存的值是code
    private final int code;

    String value;

    String desc;

    RelationalOperatorEnum(int code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
