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
public class DataType implements Serializable {

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
     * 类型名称
     */
    private String name;

    /**
     * 是否为私有的类型, true 是,false 不是私有
     */
    private Boolean isPrivate;

    /**
     * 1 整形， 2 浮点， 3 字符， 4 布尔类型
     */
    private Integer formatType;

    /**
     * 格式类型
     */
    private String jsonFormat;

    /**
     * 描述
     */
    private String dataTypeDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
