package cn.makerknz.product.server.domain.enums;

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
public enum  RoleEnum {
    ADMIN(0, "管理员"),
    PERSONAL(1,"个人"),
    FIRM(2, "公司")
    ;

    @EnumValue //标记数据库存的值是code
    private final int code;

    String desc;

    RoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 转为数据
     * @return 枚举对象数组
     */
    public static List<String> toList() {
        List<String> roles = new ArrayList<>();
        for (RoleEnum item : RoleEnum.values()) {
            roles.add(item.name());
        }
        return roles;
    }

}
