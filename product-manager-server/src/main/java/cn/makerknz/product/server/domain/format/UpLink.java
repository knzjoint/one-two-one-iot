package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上行
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 19:19
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpLink {

    private DeviceMessage deviceMessage;

    private ThingsMessage thingsMessage;

    private ThingsValue thingsValue;

}
