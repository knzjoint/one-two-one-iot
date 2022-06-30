package cn.makerknz.product.server.rule.core;

import java.util.Collection;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 8:40
 */
interface JavaRuleStorage {

    /**
     * 容器是否包含指定规则
     * @return
     */
    boolean contains(String groupName, BaseRule rule);

    /**
     * 添加规则到容器
     */
    boolean add(String groupName, BaseRule rule);

    /**
     * 批量添加规则到容器的指定组
     */
    boolean batchAdd(String groupName, Iterable<? extends BaseRule> rules);

    /**
     * 从容器移除指定规则
     */
    boolean remove(String groupName, BaseRule rule);

    /**
     * 从容器移除指定组的规则
     */
    boolean remove(String group);

    /**
     * 从容器获取指定组的所有规则
     * @return
     */
    Collection<BaseRule> listObjByGroup(String group);

}
