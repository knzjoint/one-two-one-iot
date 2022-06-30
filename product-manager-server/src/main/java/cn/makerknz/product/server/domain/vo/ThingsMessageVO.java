package cn.makerknz.product.server.domain.vo;

import cn.makerknz.product.server.domain.format.DataTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/25 18:07
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ThingsMessageVO {

    private Integer id;



    /**
     * 物值
     */
    private String thingsValue;

    /**
     * 类型
     */
    private DataTypeEnum thingsType;

}
