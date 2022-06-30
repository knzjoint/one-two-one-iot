package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.MqttUser;
import cn.makerknz.product.server.mapper.MqttUserMapper;
import cn.makerknz.product.server.service.IMqttUserService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2022-03-10
 */
@Service
@DS("emqx")
public class MqttUserServiceImpl extends ServiceImpl<MqttUserMapper, MqttUser> implements IMqttUserService {

}
