package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.ThingsLinkageLink;
import cn.makerknz.product.server.mapper.ThingsLinkageLinkMapper;
import cn.makerknz.product.server.rule.core.domain.LinkTypeEnum;
import cn.makerknz.product.server.rule.core.domain.WorkFlowForm;
import cn.makerknz.product.server.service.IThingsLinkageLinkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Enums;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2022-05-05
 */
@Service
public class ThingsLinkageLinkServiceImpl extends ServiceImpl<ThingsLinkageLinkMapper, ThingsLinkageLink> implements IThingsLinkageLinkService {

    private List<String> FLOW_ITEM_TYPE_EXCLUDE = Arrays.asList(
            "startNode",
            "endNode",
            "waitNode",
            "tempNode",
            "ifNode",
            "expandNode",
            "list"
    );

    @Override
    public void saveAllThingsLinkageLink(Integer id, List<WorkFlowForm> workFlowForms) {

        workFlowForms.stream().forEach(e -> {

            if (FLOW_ITEM_TYPE_EXCLUDE.contains(e.getType())) {
                return;
            }
            ThingsLinkageLink thingsLinkageLink = ThingsLinkageLink.builder()
                    .linkId(e.getData().getId())
                    .linkType(Enums.getIfPresent(LinkTypeEnum.class, e.getData().getLinkType()).get())
                    .thingsLinkageId(id)
                    .build();
            UpdateWrapper updateWrapper = new UpdateWrapper(thingsLinkageLink);
            this.saveOrUpdate(thingsLinkageLink, updateWrapper);
        });
    }

    @Override
    public void removeAllThingsLinkageLink(Integer id) {

        ThingsLinkageLink thingsLinkageLink = ThingsLinkageLink.builder()
                .thingsLinkageId(id)
                .build();
        QueryWrapper queryWrapper = new QueryWrapper(thingsLinkageLink);
        this.remove(queryWrapper);
    }
}
