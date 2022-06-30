package cn.makerknz.product.server.rule.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 13:33
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkFlowNodes {

    /**
     * things
     */
    private List<ThingsNode> input;

    private List<EndNode> flow;

    private List<NodeBase> action;

}
