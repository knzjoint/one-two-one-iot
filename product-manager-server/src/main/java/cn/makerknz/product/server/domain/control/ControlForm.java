package cn.makerknz.product.server.domain.control;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 包括所有的内容
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/22 14:45
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControlForm extends BaseForm {

    private String trueText;

    private String falseText;

}
