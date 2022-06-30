package cn.makerknz.product.server.rule;

import cn.makerknz.product.server.domain.format.DataTypeEnum;
import cn.makerknz.product.server.domain.format.ThingsValue;
import cn.makerknz.product.server.emqx.EmqxAdminService;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.rule.actions.ThingsControlActions;
import cn.makerknz.product.server.rule.core.domain.RuleCondition;
import cn.makerknz.product.server.service.IDeviceService;
import cn.makerknz.product.server.service.IProductService;
import cn.makerknz.product.server.service.IThingsService;
import cn.makerknz.product.server.service.impl.ThingsServiceImpl;
import com.google.gson.Gson;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.mvel2.ParserContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.io.StringReader;

/**

 [
 {
 "name": "1",
 "description": "1",
 "priority": 1,
 "compositeRuleType": "UnitRuleGroup",
 "composingRules": [
 {
 "name": "2",
 "description": "2",
 "condition": "user.getAge()<28",
 "priority": 2,
 "actions": [
 "System.out.println(\"UnitRuleGroup not ok rule2 \")"
 ]
 },
 {
 "name": "3",
 "description": "3",
 "condition": "user.name.length<10",
 "priority": 3,
 "actions": [
 "System.out.println(\"UnitRuleGroup rule3 \")"
 ]
 },
 {
 "name": "4",
 "description": "4",
 "condition": "user.name.length<10",
 "priority": 4,
 "actions": [
 "System.out.println(\"UnitRuleGroup rule4 \")"
 ]
 },
 {
 "name": "5",
 "description": "5",
 "condition": "user.name.length<10",
 "priority": 5,
 "actions": [
 "System.out.println(\"UnitRuleGroup rule5 \");UserService.doAction4(user.userinfo)"
 ]
 }
 ]
 },
 {
 "name": "3",
 "description": "3",
 "condition": "user.name.length<50",
 "priority": 3,
 "actions": [
 "System.out.println(\"default rule3 \")"
 ]
 }
 ]

 */



/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 13:03
 */

@Component
public class EasyRuleUtils implements ApplicationContextAware {

    static private MVELRuleFactory ruleFactory;

    static private Gson gson;

    static private RulesEngine rulesEngine;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EasyRuleUtils.ruleFactory = (MVELRuleFactory) applicationContext.getBean("ruleFactory");
        EasyRuleUtils.gson = (Gson) applicationContext.getBean("gson");
        EasyRuleUtils.rulesEngine = (RulesEngine) applicationContext.getBean("rulesEngine");
    }

    /**
     * 将json转换成Rules
     * @param rulesJson
     * @return
     * @throws Exception
     */
    static public Rules createRules(String rulesJson) {

        ParserContext context =new ParserContext();
        context.addImport(RuleCondition.class);
        context.addImport(ThingsServiceImpl.class);
        context.addImport(DataTypeEnum.class);
        context.addImport(ThingsControlActions.class);
        context.addImport(ThingsValue.class);

//        static IDeviceService deviceService;
//        static IProductService productService;
//        static EmqxAdminService emqxAdminService;
//        static IThingsService thingsService;
//        static Gson gson;

        context.addImport("deviceService", IDeviceService.class);
        context.addImport("productService", IProductService.class);
        context.addImport("emqxAdminService", EmqxAdminService.class);
        context.addImport("thingsService", IThingsService.class);
        context.addImport("gson", Gson.class);

        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader(), context);

        System.out.println(rulesJson);
        try {
            Reader reader  = new StringReader(rulesJson);

            return ruleFactory.createRules(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 将规则转换成Json文件
     * @param rules
     * @return
     */
    static public String rulesToJson(Rules rules) {
        return gson.toJson(rules);
    }

}
