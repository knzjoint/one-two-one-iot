package cn.makerknz.product.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTopic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品主题id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 主题ID
     */
    private String topic;

    /**
     * Topic 描述
     */
    private String topicDesc;

    /**
     * 是否可以被删除/修改操作， true 可以， false不可以
     */
    private Boolean operatorAuth;

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
