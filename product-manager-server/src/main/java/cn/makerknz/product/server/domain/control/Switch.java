package cn.makerknz.product.server.domain.control;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/21 7:14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Switch extends BaseForm {

    private String trueText;

    private String falseText;

}
