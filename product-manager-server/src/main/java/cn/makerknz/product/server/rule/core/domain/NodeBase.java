package cn.makerknz.product.server.rule.core.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 12:13
 */

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "linkType",
        visible = true)
public class NodeBase {

    /**
     * id node ID
     */
    private String id;

    /**
     * 显示的节点类型
     */
    private String type;

    private String icon;

    private String className;

    private String name;

    private String groupType;

    /**
     * 数据类型,用来反序列化使用
     */
    private String linkType;

}
