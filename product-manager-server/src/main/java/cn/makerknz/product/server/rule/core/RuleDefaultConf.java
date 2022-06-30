package cn.makerknz.product.server.rule.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 8:47
 */

@Configuration
public class RuleDefaultConf {

    @Bean
    @ConditionalOnMissingBean
    public JavaRuleStorage javaRuleStorage() {
        return new MapJavaRuleStorage();
    }

}
