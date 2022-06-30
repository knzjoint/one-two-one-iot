package cn.makerknz.product.server.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: maker_knz
 * @Date: 2022/3/24/024 9:47
 * @Version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicForm {

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 0: deny, 1: allow
     */
    private Integer allow;

    /**
     * IpAddress
     */
    private String ipaddr;

    /**
     * 1: subscribe, 2: publish, 3: pubsub
     */
    private Integer access;

    /**
     * Topic Filter
     */
    private String topic;

    /**
     * Topic 描述
     */
    private String topicDesc;

}
