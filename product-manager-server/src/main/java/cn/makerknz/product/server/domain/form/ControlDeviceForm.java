package cn.makerknz.product.server.domain.form;

import cn.makerknz.product.server.domain.format.ThingsValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/22 20:59
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControlDeviceForm extends ThingsValue {

    private Integer thingsId;

//    private String type;
//
//    private String value;

}
