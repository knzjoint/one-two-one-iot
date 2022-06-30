package cn.makerknz.product.server.domain.format;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物理单位
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/14 8:13
 */

public enum PhysicalUnitEnum {

    M(0, "米"),
    DECIMAL(2,"浮点"),
    STRING(3, "字符"),
    BOOL(4, "布尔"),
    JSON(5, "JSON类型")
    ;

    @EnumValue //标记数据库存的值是code
    private final int code;

    String desc;

    PhysicalUnitEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 转为数据
     * @return 枚举对象数组
     */
    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> roles = new ArrayList<>();
        for (PhysicalUnitEnum item : PhysicalUnitEnum.values()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("label", item.desc);
            map.put("name", item.name());
            map.put("value", item.code);
            roles.add(map);
        }
        return roles;
    }

}
