package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 19:14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThingsValue {

    /**T
     * 数据类型
     */
//    private String type;

    private DataTypeEnum type;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String desc;

}
