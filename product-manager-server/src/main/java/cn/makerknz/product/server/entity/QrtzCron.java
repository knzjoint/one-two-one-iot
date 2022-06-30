package cn.makerknz.product.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Cron类型的触发器表
 * </p>
 *
 * @author maker knz
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QrtzCron implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用token id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 物ID
     */
    private Integer thingsId;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 时区
     */
    private String timeZoneId;


}
