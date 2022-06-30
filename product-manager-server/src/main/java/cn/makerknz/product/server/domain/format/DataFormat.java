package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 17:03
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataFormat {

    private DataTypeEnum type;

    private String name;

}
