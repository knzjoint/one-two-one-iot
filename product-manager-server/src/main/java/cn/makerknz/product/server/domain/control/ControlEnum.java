package cn.makerknz.product.server.domain.control;

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
public enum ControlEnum {
    BUTTON(0, "按钮"),
    INPUT(1,"输入"),
    SLIDER(2, "滑块"),
    SWITCH(3, "切换"),
    TEXT_INPUT(4, "文本输入"),
    JSON_INPUT(5, "JSON输入")
    ;

    @EnumValue //标记数据库存的值是code
    private final int code;

    String desc;

    ControlEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 转为数据
     * @return 枚举对象数组
     */
    public static List<String> toList() {
        List<String> roles = new ArrayList<>();
        for (ControlEnum item : ControlEnum.values()) {
            roles.add(item.name());
        }
        return roles;
    }

}
