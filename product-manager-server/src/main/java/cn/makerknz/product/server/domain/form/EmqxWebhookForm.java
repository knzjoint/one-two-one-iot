package cn.makerknz.product.server.domain.form;

import cn.makerknz.product.server.domain.enums.OptsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmqxWebhookForm {

    /**
     *
     */
    private String action;

    /**
     * 客户端 ClientId
     */
    private String clientid;

    /**
     * 发布端 ClientId
     */
    private String from_client_id;

    /**
     * 客户端 Username，不存在时该值为 "undefined"
     */
    private String username;

    /**
     * 发布端 Username，不存在时该值为 "undefined"
     */
    private String fromm_username;

    /**
     * 客户端源 IP 地址
     */
    private String ipaddress;

    /**
     * 错误原因
     */
    private String reason;

    /**
     * 将订阅的主题
     */
    private String topic;

    /**
     * 将订阅的主题
     */
    private OptsEnum opt;

    /**
     * 客户端申请的心跳保活时间
     */
    private Integer keepalive;

    /**
     * 协议版本号
     */
    private Integer proto_ver;

    /**
     * "success" 表示成功，其它表示失败的原因
     */
    private String conn_ack;

    /**
     * 时间戳(秒)
     */
    private long connected_at;

    /**
     * QoS 等级，可取 0 1 2
     */
    private OptsEnum qos;

    /**
     * 是否为 Retain 消息
     */
    private boolean retain;

    /**
     * 消息 Payload
     */
    private String payload;

    /**
     * 消息的时间戳(毫秒)
     */
    private long ts;
}
