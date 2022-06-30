package cn.makerknz.product.server.rule.core.domain;

import cn.makerknz.product.server.domain.format.ThingsValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 13:15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleCondition extends ThingsValue {

//
//    /**
//     * 数据值
//     */
//    private String value;

    /**
     * 条件类型
     */
    private RelationalOperatorEnum conditionType;
}
