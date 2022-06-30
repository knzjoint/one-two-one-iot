package cn.makerknz.product.server.service;

import cn.makerknz.product.server.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
public interface IProductService extends IService<Product> {

    /**
     * 判断是否删除
     * @param id
     * @return
     */
    boolean isDeleted(Integer id);
}
