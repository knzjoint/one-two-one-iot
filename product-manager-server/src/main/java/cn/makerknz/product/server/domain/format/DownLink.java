package cn.makerknz.product.server.domain.format;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/20 19:20
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownLink {

    private ThingsValue thingsValue;

}
