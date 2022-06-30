package cn.makerknz.product.server.domain.control;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/21 7:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseForm {

    /**
     * 一个英文名，也是主题的最后一段
     */
    private String topic;

    private ControlEnum controlType;

    private String type;

    private String value;

    private String name;

}
