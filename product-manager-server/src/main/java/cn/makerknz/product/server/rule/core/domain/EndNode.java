package cn.makerknz.product.server.rule.core.domain;

import cn.makerknz.product.server.entity.Things;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
@JsonTypeName(value = "END_TYPE")
public class EndNode  extends NodeBase {

    private Things data;

    private RuleCondition condition;

}
