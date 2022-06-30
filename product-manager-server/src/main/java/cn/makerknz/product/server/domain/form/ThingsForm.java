package cn.makerknz.product.server.domain.form;

import cn.makerknz.product.server.domain.control.ControlEnum;
import cn.makerknz.product.server.domain.format.DataFormatForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/22 12:05
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThingsForm {

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
     * 产品类型id
     */
    private Integer dataTypeId;

//    /**
//     * 数据格式详情
//     */
    private DataFormatForm dataFormat;

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

    private ControlEnum controlType;

}
