package cn.makerknz.product.server.rule;

import cn.makerknz.product.server.entity.Things;
import cn.makerknz.product.server.entity.ThingsLinkage;
import cn.makerknz.product.server.entity.ThingsLinkageLink;
import cn.makerknz.product.server.service.IThingsLinkageLinkService;
import cn.makerknz.product.server.service.IThingsLinkageService;
import cn.makerknz.product.server.service.IThingsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/5 14:20
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EasyRuleUtilsTest {

    @Autowired
    private IThingsLinkageService thingsLinkageService;

    @Autowired
    private IThingsLinkageLinkService thingsLinkageLinkService;

    @Autowired
    private IThingsService thingsService;

    @Autowired
    private RulesEngine rulesEngine;

    @Test
    void createRules() {
        ThingsLinkage linkage = thingsLinkageService.getById(1);

        String rulesString = linkage.getRule();
        Rules rules = EasyRuleUtils.createRules(rulesString);
        Facts facts = new Facts();

        QueryWrapper<ThingsLinkageLink> queryWrapper = new QueryWrapper<>();
        List<ThingsLinkageLink> list = this.thingsLinkageLinkService.list(queryWrapper);
        List<Integer> collect = list.stream().map(e -> e.getLinkId()).collect(Collectors.toList());
        List<Things> things = this.thingsService.listByIds(collect);
        things.stream().forEach(e -> {
            String name = "THINGS_" + e.getId() + "_" + e.getSequence();
            facts.put(name, 1312313.0);
        });

        rulesEngine.fire(rules, facts);
    }
}