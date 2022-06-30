package cn.makerknz.product.server.domain.vo;

import cn.makerknz.product.server.domain.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 朱康南
 * @Date: 2021/4/18/018 15:44
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private Integer id;
    /**
     * 真实姓名
     */
    private String truename;

    private String phone;

    private String avatar;

    /**
     * 角色0-管理员,1-个人用户,2-公司
     */
    private RoleEnum role;

    /**
     * 省份
     */
    private List<String> provinceCityDistrict;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区/县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

}
