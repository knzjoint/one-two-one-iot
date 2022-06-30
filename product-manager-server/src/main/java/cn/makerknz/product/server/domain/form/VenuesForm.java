package cn.makerknz.product.server.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/25 20:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenuesForm {

    /**
     * 场地 id
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 场地名称
     */
    private String venuesName;

    /**
     * 做树形结构，0时，为最顶层
     */
    private Integer parentId;

    /**
     * 场地描述
     */
    private String venuesDesc;

    private String venuesIcon;

    private List<Integer> deviceIds;

}
