package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 16:52
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StringDataFormat extends DataFormat {

    private Integer maxLength = 1024;

//    private String type = "String";

}
