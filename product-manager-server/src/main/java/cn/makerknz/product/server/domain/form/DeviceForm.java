package cn.makerknz.product.server.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: maker_knz
 * @Date: 2022/3/10/010 11:13
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceForm {

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码,未加密
     */
    private String password;

    /**
     * mqtt设备的clientId
     */
    private String clientId;

}
