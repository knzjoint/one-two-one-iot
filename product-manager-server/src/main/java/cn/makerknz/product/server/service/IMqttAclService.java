package cn.makerknz.product.server.service;

import cn.makerknz.product.server.entity.MqttAcl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maker knz
 * @since 2022-03-23
 */
public interface IMqttAclService extends IService<MqttAcl> {

    boolean delete(QueryWrapper<MqttAcl> queryWrapper);

}
