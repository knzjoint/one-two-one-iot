package cn.makerknz.product.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author maker knz
 * @since 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MqttAcl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 产品主题ID
     */
    private Integer productTopicId;

    /**
     * 0: deny, 1: allow
     */
    private Integer allow;

    /**
     * IpAddress
     */
    private String ipaddr;

    /**
     * Username
     */
    private String username;

    /**
     * ClientId
     */
    private String clientid;

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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updateTime;


}
