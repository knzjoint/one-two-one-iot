package cn.makerknz.product.server.domain.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: maker_knz
 * @Date: 2022/3/22/022 16:50
 * @Version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductParam {

    /**
     * 产品的英文名称用来限制topic
     */
    private String productEnName;

}
