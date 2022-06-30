package cn.makerknz.product.server.rule.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/3 10:49
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkFlowForm {

    /**
     * id node ID  clientId#sequence
     */
    private String id;

    /**
     * 在jsplusm中唯一的标识
     */
    private String uuid;

    private String name;

    /**
     * 类型 startNode
     */
    private String type;

    private String groupType;

    /**
     * 是否显示
     */
    private Boolean hidden;

    private float left;

    private float top;

    private String className;

    /**
     * 下一个节点数
     */
    private List<String> next;

    /**
     * 前节点数组
     */
    private List<String> prev;

    private LinkTypeEnum linkType;

    private FormData formData;

    /**
     * 根据数据类型进行反序列化
     */
    private DataBase data;

    private RuleCondition condition;
}
