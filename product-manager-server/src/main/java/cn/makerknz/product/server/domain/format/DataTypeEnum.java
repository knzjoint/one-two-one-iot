package cn.makerknz.product.server.domain.format;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: maker_knz
 * @Date: 2022/3/4/004 10:11
 * @Version 1.0
 */

@Getter
public enum DataTypeEnum {
    INT(1, "整数"),
    DECIMAL(2,"浮点"),
    STRING(3, "字符"),
    BOOL(4, "布尔"),
    JSON(5, "JSON类型")
    ;

    @EnumValue //标记数据库存的值是code
    private final int code;

    String desc;

    DataTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 转为数据
     * @return 枚举对象数组
     */
    public static List<String> toList() {
        List<String> roles = new ArrayList<>();
        for (DataTypeEnum item : DataTypeEnum.values()) {
            roles.add(item.name());
        }
        return roles;
    }

}
