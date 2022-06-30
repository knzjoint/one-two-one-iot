package cn.makerknz.product.server.domain.form;

import cn.makerknz.product.server.domain.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 朱康南
 * @Date: 2021/4/20/020 11:51
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {
    /**
     * 真实姓名
     */
    private String truename;

    private String phone;

    private String password;

    /**
     * 角色0-管理员,1-普通用户,2-工作人员
     */
    private RoleEnum role;

    /**
     * 省份
     */
    private List<String> provinceCityDistrict;

    /**
     * 详细地址
     */
    private String address;

}
