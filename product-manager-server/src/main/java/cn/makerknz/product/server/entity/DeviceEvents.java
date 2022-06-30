package cn.makerknz.product.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("device_events")
@EqualsAndHashCode(callSuper = false)
public class DeviceEvents implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备事件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    /**
     * 消息的时间
     */
    private LocalDateTime msgTime;

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 设备ID
     */
    private Integer deviceId;

    /**
     * 数据类型:  1:event  2:response
     */
    private Integer dataType;

    /**
     * 主题
     */
    private String topic;

    /**
     * 事件类型
     */
    private String eventAction;

    /**
     * 数据流ID
     */
    private String streamId;

    /**
     * 接收的数据
     */
    private String data;

    /**
     * 返回的数据
     */
    private String responseResult;

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
