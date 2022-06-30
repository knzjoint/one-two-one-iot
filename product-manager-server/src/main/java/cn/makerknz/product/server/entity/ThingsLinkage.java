package cn.makerknz.product.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class ThingsLinkage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用token id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 场地ID
     */
    private Integer venuesId;

    /**
     * 联动流程图用于生成规则
     */
    private String controlUi;

    /**
     * 规则
     */
    private String rule;

    /**
     * true为启动，false关闭
     */
    private Boolean isStartup;

    /**
     * 联动场景名称
     */
    private String name;

    /**
     * 联动场景描述
     */
    private String linkageDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
