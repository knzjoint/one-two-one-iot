package cn.makerknz.product.server.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: maker_knz
 * @Date: 2022/3/10/010 10:23
 * @Version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {

    /**
     * 用户ID
     */
    private Integer userId;

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

}
