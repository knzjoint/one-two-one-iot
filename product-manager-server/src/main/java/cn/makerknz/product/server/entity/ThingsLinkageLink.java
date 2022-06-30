package cn.makerknz.product.server.entity;

import cn.makerknz.product.server.rule.core.domain.LinkTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author maker knz
 * @since 2022-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThingsLinkageLink implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 联动关联的条件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 联动 id
     */
    private Integer thingsLinkageId;

    /**
     * 关联的ID
     */
    private Integer linkId;

    /**
     * 关联的类型
     */
    private LinkTypeEnum linkType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
