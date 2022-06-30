package cn.makerknz.product.server.rule.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/2 13:21
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeMsg {

    private String nodeName;

    private String icon;

    private Integer width;

    private Integer height;

    private Integer x;

    private Integer y;

}
