package cn.makerknz.product.server.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/5/9 11:59
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThingsLinkageForm {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 场地ID
     */
    private Integer venuesId;

//    /**
//     * 联动流程图用于生成规则
//     */
//    private String controlUi;
//
//    /**
//     * 规则
//     */
//    private String rule;

    /**
     * true为启动，false关闭
     */
    private Boolean isStartup;

    /**
     * 联动场景名称
     */
    private String name;

    /**
     * 联动场景描述
     */
    private String linkageDesc;

}
