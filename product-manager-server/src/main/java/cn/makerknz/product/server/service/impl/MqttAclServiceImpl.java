package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.MqttAcl;
import cn.makerknz.product.server.mapper.MqttAclMapper;
import cn.makerknz.product.server.service.IMqttAclService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2022-03-23
 */
@Service
@DS("emqx")
public class MqttAclServiceImpl extends ServiceImpl<MqttAclMapper, MqttAcl> implements IMqttAclService {

    @Override
    public boolean delete(QueryWrapper<MqttAcl> queryWrapper) {
        return false;
    }
}
