package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 16:51
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecimalDataFormat extends DataFormat {

    private Float min;

    private Float max;

    private Float step;

//    private String type = "Decimal";

}
