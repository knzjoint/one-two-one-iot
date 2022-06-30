package cn.makerknz.product.server.domain.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: maker_knz
 * @Date: 2022/3/10/010 11:02
 * @Version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceParam {

    /**
     * 产品ID
     */
    private List<Integer> productName;

    /**
     * 在线状态
     */
    private List<Boolean> connectStatus;

    /**
     *
     */
    private Integer userId;

}
