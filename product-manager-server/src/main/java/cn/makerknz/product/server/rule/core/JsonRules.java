package cn.makerknz.product.server.rule.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/5 10:44
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JsonRules {

    private String name;

    private String description;

    private Integer priority;

    private String compositeRuleType;

    private List<JsonRule> composingRules;
}
