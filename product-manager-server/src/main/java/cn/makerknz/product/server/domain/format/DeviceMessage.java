package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 19:36
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceMessage {

    /***
     * 设备名
     */
    private String deviceName;

    /**
     * 固件名
     */
    private String firmware;

    /**
     * 固件版本
     */
    private String version;

}
