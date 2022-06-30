package cn.makerknz.product.server.emqx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/23 7:11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThingsPayloadVO {

    /**
     * mqtt设备的clientId
     */
    private String clientId;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 版本号
     */
    private String version;

    /**
     * 产品队列
     */
    private Integer sequence;

    /**
     * 物图链接
     */
    private String thingsIcon;

    /**
     * 描述
     */
    private String thingsDesc;

    private Message message;

}
