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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntDataFormat extends DataFormat {

    private Integer min;

    private Integer max;

    private Integer step;

//    private String type = "Int";

}
