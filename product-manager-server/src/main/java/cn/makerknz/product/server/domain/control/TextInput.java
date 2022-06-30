package cn.makerknz.product.server.domain.control;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/21 7:15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextInput extends BaseForm {

    private Integer maxLength = 1024;

}
