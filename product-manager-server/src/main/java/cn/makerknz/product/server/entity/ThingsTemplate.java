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
public class ThingsTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物产品类型模板 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 是否为私有的类型, true 是,false 不是私有
     */
    private Boolean isPrivate;

    /**
     * 产品类型id
     */
    private Integer dataTypeId;

    /**
     * 数据格式详情
     */
    private String dataFormat;

    /**
     * 物名称
     */
    private String thingsName;

    /**
     * 物图链接
     */
    private String thingsIcon;

    /**
     * 描述
     */
    private String thingsDesc;

    /**
     * 是否显示，true 显示
     */
    private Boolean isShow;

    /**
     * 1 是只读, 2 只写， 3 读写
     */
    private Integer rwType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
