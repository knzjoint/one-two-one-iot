package cn.makerknz.product.server.rule.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 8:42
 */
public class MapJavaRuleStorage implements JavaRuleStorage {

    private final Multimap<String, BaseRule> map = HashMultimap.create();

    @Override
    public boolean contains(String groupName, BaseRule rule) {
        return map.containsEntry(groupName, rule);
    }

    @Override
    public boolean add(String groupName, BaseRule rule) {
        // 如果原来有，就先删除掉
        if (map.containsEntry(groupName, rule)) {
            map.remove(groupName, rule);
        }
        return map.put(groupName, rule);
    }

    @Override
    public boolean batchAdd(String groupName, Iterable<? extends BaseRule> rules) {
        return map.putAll(groupName, rules);
    }

    @Override
    public boolean remove(String groupName, BaseRule rule) {
        return map.remove(groupName, rule);
    }

    @Override
    public boolean remove(String group) {
        return !map.removeAll(group).isEmpty();
    }

    @Override
    public Collection<BaseRule> listObjByGroup(String group) {
        return map.get(group);
    }

}
