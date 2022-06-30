package cn.makerknz.product.server.domain.control;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/21 7:13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Button extends BaseForm {

    private String trueText;

    private String falseText;

}
