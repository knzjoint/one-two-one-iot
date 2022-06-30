package cn.makerknz.product.server.controller;

import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.domain.form.ProductForm;
import cn.makerknz.product.server.domain.param.ProductParam;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.entity.MqttAcl;
import cn.makerknz.product.server.entity.Product;
import cn.makerknz.product.server.entity.ProductTopic;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.service.*;
import cn.makerknz.product.server.utils.TopicUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IMqttAclService mqttAclService;

    @Autowired
    private IMqttUserService mqttUserService;

    @Autowired
    private IProductTopicService productTopicService;

    @Autowired
    private IDeviceService deviceService;

    @GetMapping("/{id}")
    @CheckLogin
    ResultVO productOne(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        Product product = productService.getById(id);
        if (product == null) {
            throw new BusinessException(ExceptionEnum.PRODUCT_NOTFOUND);
        }

        if (!userId.equals(product.getUserId())) {
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }

        return ResultVO.success(product);
    }

    /**
     * 获取用户
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/list")
    @CheckLogin
    ResultVO getList(HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("deleted", false);
        productQueryWrapper.eq("user_id", userId);
        List<Product> productList = productService.list(productQueryWrapper);

        return ResultVO.success(productList);
    }

    /**
     * 获取用户
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/page")
    @CheckLogin
    ResultVO getPage(Page page, ProductParam productParam, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        Product product = Product.builder()
                .userId(userId)
                .deleted(false)
                .build();
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>(product);
        productQueryWrapper.like(productParam.getProductEnName() != null, "product_en_name", productParam.getProductEnName());
        Page productPage = productService.page(page, productQueryWrapper);

        return ResultVO.success(productPage);
    }

    /**
     * 添加一个产品
     *
     * @return
     */
    @PostMapping("/add-product")
    @CheckLogin
    ResultVO addProduct(@RequestBody ProductForm productForm, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        // 创建产品
        Product product = new Product();
        BeanUtils.copyProperties(productForm, product);
        product.setDeleted(false);
        product.setUserId(userId);

        boolean save = productService.save(product);
        if (!save) {
            throw new BusinessException(ExceptionEnum.PRODUCT_ADD_ERROR);
        }

        // 创建产品的默认主题
        ProductTopic subTopicAcl1 = ProductTopic.builder()
                .access(1)
                .productId(product.getId())
                .topic(TopicUtils.subTopic(productForm.getProductEnName()))
                .allow(1)
                .operatorAuth(false)
                .topicDesc("接受指令的主题")
                .build();
        boolean aclSave_1 = productTopicService.save(subTopicAcl1);
        if (!aclSave_1) {
            this.productService.removeById(product.getId());
            throw new BusinessException(ExceptionEnum.PRODUCT_ADD_ERROR);
        }
        ProductTopic subTopicAcl2 = ProductTopic.builder()
                .access(2)
                .productId(product.getId())
                .topic(TopicUtils.pubTopic(productForm.getProductEnName()))
                .allow(1)
                .operatorAuth(false)
                .topicDesc("发布主题")
                .build();
        boolean aclSave_2 = productTopicService.save(subTopicAcl2);
        if (!aclSave_2) {
            this.productTopicService.removeById(subTopicAcl1.getId());
            this.productService.removeById(product.getId());
            throw new BusinessException(ExceptionEnum.PRODUCT_ADD_ERROR);
        }

        ProductTopic subTopicAcl3 = ProductTopic.builder()
                .access(3)
                .productId(product.getId())
                .topic(TopicUtils.thingsTopic(productForm.getProductEnName()))
                .allow(1)
                .operatorAuth(false)
                .topicDesc("物控制")
                .build();
        boolean aclSave_3 = productTopicService.save(subTopicAcl3);
        if (!aclSave_3) {
            this.productTopicService.removeById(subTopicAcl1.getId());
            this.productTopicService.removeById(subTopicAcl2.getId());
            this.productService.removeById(product.getId());
            throw new BusinessException(ExceptionEnum.PRODUCT_ADD_ERROR);
        }

        return ResultVO.success(product);
    }

    /**
     * 删除设备
     *
     * @param id                 设备ID
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/{id}")
    @CheckLogin
    ResultVO deleteProduct(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        Product product = this.productService.getById(id);
        if (product == null) {
            throw new BusinessException(ExceptionEnum.PRODUCT_NOTFOUND);
        }
        if (!product.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }
        product.setDeleted(true);
        this.productService.updateById(product);

        // TODO EMQX中的账号和主题删除
        // 给所有设备添加
        deviceService.findByProductId(id)
                .toStream()
                .forEach(e -> {
                    // 更新设备为删除状态
                    Device newDevice = new Device();
                    BeanUtils.copyProperties(e, newDevice);
                    newDevice.setDeleted(true);
                    deviceService.updateById(newDevice);
                    // 真实的删除
                    mqttUserService.removeById(e.getMqttUserId());
                });
        // 删除所有的主题
        QueryWrapper<MqttAcl> mqttAclQueryWrapper = new QueryWrapper<>();
        mqttAclQueryWrapper.eq("product_id", id);
        mqttAclService.remove(mqttAclQueryWrapper);

        return ResultVO.success();
    }

}
