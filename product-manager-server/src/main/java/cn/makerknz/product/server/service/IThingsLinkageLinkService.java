package cn.makerknz.product.server.service;

import cn.makerknz.product.server.entity.ThingsLinkageLink;
import cn.makerknz.product.server.rule.core.domain.WorkFlowForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maker knz
 * @since 2022-05-05
 */
public interface IThingsLinkageLinkService extends IService<ThingsLinkageLink> {

    /**
     *
     * @param id 场景联动的ID
     * @param workFlowForms
     */
    void saveAllThingsLinkageLink(Integer id, List<WorkFlowForm> workFlowForms);

    /**
     *
     * @param id 场景联动的ID
     */
    void removeAllThingsLinkageLink(Integer id);

}
