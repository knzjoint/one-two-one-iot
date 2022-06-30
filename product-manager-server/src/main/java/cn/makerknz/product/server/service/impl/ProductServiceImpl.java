package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.Product;
import cn.makerknz.product.server.mapper.ProductMapper;
import cn.makerknz.product.server.service.IProductService;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Override
    public boolean isDeleted(Integer id) {
        Product product = this.baseMapper.selectById(id);
        if (product.getDeleted()) {
            throw new RuntimeException("产品已经被删除");
        }
        return product.getDeleted();
    }

}
