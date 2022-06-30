package cn.makerknz.product.server.rule;

import cn.makerknz.product.server.domain.form.EmqxWebhookForm;
import cn.makerknz.product.server.domain.format.UpLink;
import cn.makerknz.product.server.entity.Things;
import cn.makerknz.product.server.entity.ThingsEvent;
import cn.makerknz.product.server.service.IThingsEventService;
import cn.makerknz.product.server.service.IThingsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 19:24
 */

@Service
public class RuleService {

    @Autowired
    private Gson gson;

//    @Autowired
//    private MVELRuleFactory ruleFactory;

    @Autowired
    private IThingsService thingsService;

    @Autowired
    private IThingsEventService thingsEventService;

    @Autowired
    private RulesEngine rulesEngine;

    /**
     * 处理的是订阅信息
     * @param emqxWebhookForm
     */
    public void processOn(EmqxWebhookForm emqxWebhookForm){
        String payload = emqxWebhookForm.getPayload();
        UpLink upLink = gson.fromJson(payload, UpLink.class);

        // 加载规则引擎
        String topic = emqxWebhookForm.getTopic();
        String[] topicSplit = topic.split("/");
        String clientId = topicSplit[1];
        String thingsName = topicSplit[3];
        String sequence = topicSplit[4];

        // 查找是否存在物模型
        // TODO 如果不在的话可以
        QueryWrapper thingsWrapper = new QueryWrapper<Things>();
        thingsWrapper.eq("client_id", clientId);
        thingsWrapper.eq("things_name", thingsName);
        thingsWrapper.eq("sequence", sequence);
        Things things = thingsService.getOne(thingsWrapper);

        // 查找事件
        QueryWrapper thingsEventWrapper = new QueryWrapper<ThingsEvent>();
        thingsEventWrapper.eq("things_id", things.getId());
        ThingsEvent thingsEvent = thingsEventService.getOne(thingsEventWrapper);

        // 规则引擎
        Rules rules = EasyRuleUtils.createRules(thingsEvent.getRule());
        // define facts
        Facts facts = new Facts();
        facts.put(thingsName, upLink.getThingsValue().getValue());
        rulesEngine.fire(rules, facts);
    }

}
