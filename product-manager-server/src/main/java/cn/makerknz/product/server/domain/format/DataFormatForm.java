package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/22 16:19
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataFormatForm extends DataFormat {

    private String trueText;

    private String falseText;

    private String min;

    private String max;

    private String step;

    private Integer maxLength = 1024;

}
