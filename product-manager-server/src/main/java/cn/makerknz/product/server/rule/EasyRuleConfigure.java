package cn.makerknz.product.server.rule;

import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 15:02
 */

@Configuration
public class EasyRuleConfigure {

    @Bean
    @ConditionalOnMissingBean
    public MVELRuleFactory ruleFactory() {
        return new MVELRuleFactory(new JsonRuleDefinitionReader());
    }

    @Bean
    @ConditionalOnMissingBean
    public RulesEngine rulesEngine() {
        return new DefaultRulesEngine();
    }

}
