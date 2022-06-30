package cn.makerknz.product.server.rule;

import cn.makerknz.product.server.domain.format.DataTypeEnum;
import cn.makerknz.product.server.entity.Things;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.rule.core.JsonRule;
import cn.makerknz.product.server.rule.core.domain.*;
import cn.makerknz.product.server.service.IThingsService;
import com.google.gson.Gson;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 13:48
 */

@Component
public class WorkFlowUtils implements ApplicationContextAware {

    static private IThingsService thingsService;

    static private Gson gson;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WorkFlowUtils.thingsService = (IThingsService) applicationContext.getBean("thingsServiceImpl");
        WorkFlowUtils.gson = (Gson) applicationContext.getBean("gson");
    }

    /**
     * 创建一个物Node
     *
     * @param things
     * @return
     */
    static public ThingsNode createThingsNode(Things things) {

        ThingsNode thingsNode = new ThingsNode();
        thingsNode.setLinkType("THINGS_TYPE");
        thingsNode.setData(things);
        thingsNode.setClassName("step-tag");
        thingsNode.setGroupType("input");
        thingsNode.setId(WorkFlowUtils.createNodeId("input", things));
        thingsNode.setName(things.getName());
        thingsNode.setType("nodeNode");
        thingsNode.setIcon(things.getThingsIcon());

        return thingsNode;
    }


    /**
     * 创建一个Flow  Node
     *
     * @param things
     * @return
     */
    static public EndNode createFlowNode(Things things) {

        EndNode endNode = new EndNode();
        endNode.setLinkType("THINGS_TYPE");
        endNode.setData(things);
        endNode.setClassName("step-end");
        endNode.setGroupType("flow");
        endNode.setId(WorkFlowUtils.createNodeId("flow", things));
        endNode.setName(things.getName());
        endNode.setType("endNode");
        endNode.setIcon(things.getThingsIcon());

        return endNode;
    }


    static public String createNodeId(String group, Things things) {
        return group + "#" + things.getClientId() + "#" + things.getSequence();
    }

    /**
     * // TODO　先支持串联功能
     *
     * @param workFlowForms
     * @return
     */
    static public String workFlowUiToRule(List<WorkFlowForm> workFlowForms) {
        // 获取首节点
        List<WorkFlowForm> endWorkFlow = workFlowForms.stream().filter(e -> e.getType().equals("endNode"))
                .collect(Collectors.toList());
        if (endWorkFlow.size() <= 0) {
            throw new BusinessException(ExceptionEnum.RULE_NOT_FOUND_END_NODE);
        }

        String condition = createSeriesCondition(endWorkFlow.get(0), workFlowForms);

        String action = createAction(endWorkFlow.get(0));
        List<String> actions = new ArrayList<>();
        actions.add(action);

        JsonRule jsonRule = JsonRule.builder()
                .name("测试")
                .description("测试")
                .condition(condition)
                .priority(1)
                .actions(actions)
                .build();

        List<JsonRule> subRules = new ArrayList<>();
        subRules.add(jsonRule);
        List<JsonRule> mainRules = new ArrayList<>();
        JsonRule jsonRules = JsonRule.builder()
                .name("测试规则组!")
                .description("测试规则组!")
                .priority(1)
                .compositeRuleType("UnitRuleGroup")
                .composingRules(subRules)
                .build();
        mainRules.add(jsonRules);

        return gson.toJson(mainRules);
    }

    static public String createParallelCondition(List<WorkFlowForm> nodes, List<WorkFlowForm> workFlowForms) {
        Integer size = nodes.size();
        IntStream.range(0, size).forEach(i -> {

            if (i == (size - 1)) {
//                workFlowForms.
            }

            List<WorkFlowForm> endWorkFlow = workFlowForms.stream().filter(e -> e.getType() == "endNode")
                    .collect(Collectors.toList());

            System.out.println(nodes.get(i));
        });

        return null;
    }

    static public String createSeriesCondition(WorkFlowForm node, List<WorkFlowForm> workFlowForms) {
        List<String> prevs = node.getPrev();

        if (prevs.size() == 1) {
            String uuid = prevs.get(0);
            List<WorkFlowForm> list = workFlowForms.stream().filter(e -> e.getUuid().equals(uuid)).collect(Collectors.toList());
            if (list.size() == 1) {
                WorkFlowForm workFlowForm = list.get(0);
                if (workFlowForm.getType().equals("startNode")) {
                    return "";
                }
                String seriesCondition = WorkFlowUtils.createSeriesCondition(workFlowForm, workFlowForms);
                if (seriesCondition.isEmpty()) {
                    return WorkFlowUtils.condition(workFlowForm);
                }
                return seriesCondition + " && " + WorkFlowUtils.condition(workFlowForm);
            }
        } else if (prevs.size() > 1) {

        } else if (prevs.size() == 0) {
            throw new RuntimeException("请检验UI图形");
        }
        return "";
    }

    static public String condition(WorkFlowForm workFlowForm) {
        ThingData thingData = (ThingData) workFlowForm.getData();
        String name = "THINGS_" + thingData.getId() + "_" + thingData.getSequence();
        RuleCondition condition = workFlowForm.getCondition();

        String result = "";
        if (DataTypeEnum.BOOL.equals(thingData.getThingsType())) {
            if (RelationalOperatorEnum.EQ.equals(condition.getConditionType())) {
                result = name + " == " + condition.getValue();
            } else {
                result = name + " != " + condition.getValue();
            }
        } else if (DataTypeEnum.DECIMAL.equals(thingData.getThingsType())) {
            return "Float.compare(" + name + ", " + condition.getValue() + ") " + condition.getConditionType().getValue() + " 0";
        } else if (DataTypeEnum.STRING.equals(thingData.getThingsType())) {
            return name + condition.getValue() + condition.getValue();
        } else if (DataTypeEnum.INT.equals(thingData.getThingsType())) {
            return "Integercompare(" + name + ", " + condition.getValue() + ") " + condition.getConditionType().getValue() + " 0";
        } else {

        }
        return result;
    }


    static public String createAction(WorkFlowForm endFlowForm) {
        ThingData thingData = (ThingData) endFlowForm.getData();
        Things things = thingsService.getById(thingData.getId());
        if (things == null) {
            throw new BusinessException(ExceptionEnum.THINGS_USER_NO_PERMISSION);
        }

        RuleCondition condition = endFlowForm.getCondition();
        if (condition == null) {
            throw new BusinessException(ExceptionEnum.THINGS_USER_NO_PERMISSION);
        }

        String result = "ThingsValue ruleCondition = new ThingsValue();" +
                "ruleCondition.setType(DataTypeEnum." + condition.getType() + ");" +
                "ruleCondition.setValue(" + condition.getValue() + ");" +
                "System.out.println(ruleCondition.toString());" +
                "ThingsControlActions.ruleEngineControl(" + things.getId() + ", ruleCondition);"
                ;

        return result;
    }

}
