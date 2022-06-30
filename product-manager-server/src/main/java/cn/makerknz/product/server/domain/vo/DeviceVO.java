package cn.makerknz.product.server.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: maker_knz
 * @Date: 2022/3/22/022 11:01
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceVO {

    private Integer id;

    /**
     * 用户ID
     */
    private long userId;

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * mqtt设备名称ID
     */
    private Integer mqttUserId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码,未加密
     */
    private String password;

    /**
     * mqtt设备的clientId
     */
    private String clientId;

    /**
     * 连接状态，false，true 连接
     */
    private Boolean connectStatus;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品的英文名称用来限制topic
     */
    private String productEnName;

    /**
     * 产品描述
     */
    private String productDesc;

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
