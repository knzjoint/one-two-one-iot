package cn.makerknz.product.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author maker knz
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ThingsEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物事件 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 物ID
     */
    private Integer thingsId;

    /**
     * 规则
     */
    private String rule;

    /**
     * true为启动，false关闭
     */
    private Boolean isStartup;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
