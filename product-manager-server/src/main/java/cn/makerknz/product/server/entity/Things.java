package cn.makerknz.product.server.entity;

import cn.makerknz.product.server.domain.format.DataTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Things implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 传感器/控制器 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * mqtt设备的clientId
     */
    private String clientId;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 版本号
     */
    private String version;

    /**
     * 产品队列
     */
    private Integer sequence;

    /**
     * 产品类型id
     */
    private Integer dataTypeId;

    /**
     * 数据格式详情
     */
    private String dataFormat;

    /**
     * 物名称（英文）
     */
    private String thingsName;

    /**
     * 中文名称简称
     */
    private String name;

    /**
     * 物图链接
     */
    private String thingsIcon;

    /**
     * 描述
     */
    private String thingsDesc;

    /**
     * 物值
     */
    private String thingsValue;

    /**
     * 类型
     */
    private DataTypeEnum thingsType;

    /**
     * 控制ui
     */
    private String controlUi;

    /**
     * 是否显示，true 显示
     */
    private Boolean isShow;

    /**
     * 1 是只读, 2 只写， 3 读写
     */
    private Integer rwType;

    /**
     * true为启动，false关闭
     */
    private Boolean isStartup;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
