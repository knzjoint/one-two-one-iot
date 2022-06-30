package cn.makerknz.product.server.emqx;

import cn.makerknz.product.server.domain.format.DataTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/23 7:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private DataTypeEnum type;

    private String value;

}
