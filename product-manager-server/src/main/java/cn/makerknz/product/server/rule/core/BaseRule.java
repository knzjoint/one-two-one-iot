package cn.makerknz.product.server.rule.core;

import org.jeasy.rules.annotation.Priority;

import java.util.Objects;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 8:36
 */
public class BaseRule {

    private Integer priority = Integer.MAX_VALUE;

    /*重写equals方法和hashCode方法，让Set集合判定同类型的两个对象相同*/

    @Override
    public boolean equals(Object obj) {
        return Objects.nonNull(obj)
                && Objects.equals(this.getClass().getName(), obj.getClass().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getClass().getName());
    }

    /**
     * 获取优先级
     */
    @Priority
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
