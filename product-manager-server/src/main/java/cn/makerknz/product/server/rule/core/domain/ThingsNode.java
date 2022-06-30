package cn.makerknz.product.server.rule.core.domain;

import cn.makerknz.product.server.entity.Things;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 12:11
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//@JsonTypeName(value = "THINGS_TYPE")
public class ThingsNode extends NodeBase {

    private Things data;

    private RuleCondition condition;

}
