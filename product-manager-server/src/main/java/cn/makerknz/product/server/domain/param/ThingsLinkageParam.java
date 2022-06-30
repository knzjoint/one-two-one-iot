package cn.makerknz.product.server.domain.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/9 9:46
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThingsLinkageParam {

    /**
     * 场地ID
     */
    private Integer venuesId;


    /**
     * true为启动，false关闭
     */
    private Boolean isStartup;

}
