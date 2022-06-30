package cn.makerknz.product.server.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: maker_knz
 * @Date: 2022/3/4/004 15:34
 * @Version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationTokenForm {

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 应用名称
     */
    private String applicationName;

}
