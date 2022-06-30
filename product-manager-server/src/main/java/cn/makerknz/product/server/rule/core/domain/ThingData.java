package cn.makerknz.product.server.rule.core.domain;

import cn.makerknz.product.server.domain.format.DataTypeEnum;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/3 18:05
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonTypeName(value = "THINGS_TYPE")
public class ThingData extends DataBase {


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

}
