package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.ProductTopic;
import cn.makerknz.product.server.mapper.ProductTopicMapper;
import cn.makerknz.product.server.service.IProductTopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@Service
public class ProductTopicServiceImpl extends ServiceImpl<ProductTopicMapper, ProductTopic> implements IProductTopicService {
}
