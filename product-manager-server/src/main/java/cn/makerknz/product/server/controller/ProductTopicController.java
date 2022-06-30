package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.domain.form.TopicForm;
import cn.makerknz.product.server.entity.MqttAcl;
import cn.makerknz.product.server.entity.Product;
import cn.makerknz.product.server.entity.ProductTopic;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.service.IDeviceService;
import cn.makerknz.product.server.service.IMqttAclService;
import cn.makerknz.product.server.service.IProductService;
import cn.makerknz.product.server.service.IProductTopicService;
import cn.makerknz.product.server.utils.TopicUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/product-topic")
@Slf4j
public class ProductTopicController {

    @Autowired
    private IProductTopicService productTopicService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IMqttAclService mqttAclService;

    @Autowired
    private IDeviceService deviceService;

    @GetMapping("/list/{id}")
    @CheckLogin
    public ResultVO topicList(@PathVariable("id") Integer productId, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer)httpServletRequest.getAttribute("id");

        Product product = productService.getById(productId);
        if(!product.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }

        QueryWrapper<ProductTopic> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", productId);
        List<ProductTopic> mqttAcls = productTopicService.list(queryWrapper);

        // 修改主题内容
        List<ProductTopic> mqttAclList = mqttAcls.stream().map(e -> {
            e.setTopic(TopicUtils.replaceToDevice(e.getTopic()));
            return e;
        }).collect(Collectors.toList());

        return ResultVO.success(mqttAclList);
    }

    /**
     * 添加一个主题
     * @return
     */
    @PostMapping("/add-topic")
    @CheckLogin
    ResultVO addTopic(@RequestBody TopicForm topicForm, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer)httpServletRequest.getAttribute("id");

        ProductTopic topic = this.createTopic(topicForm, userId, false);

        boolean saved = this.productTopicService.save(topic);
        if (!saved) {
            throw new BusinessException(ExceptionEnum.PRODUCT_ADD_TOPIC_ERROR);
        }

        MqttAcl mqttAcl = new MqttAcl();
        BeanUtils.copyProperties(topic, mqttAcl);
        mqttAcl.setProductTopicId(topic.getId());

        // 给所有设备添加
        deviceService.findByProductId(topic.getProductId())
                .toStream()
                .forEach(e -> {
                    mqttAcl.setId(null);
                    mqttAcl.setClientid(e.getClientId());
                    mqttAclService.save(mqttAcl);
                });

        return ResultVO.success();
    }

    /**
     * 删除一个topic
     * @param id
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/{id}")
    @CheckLogin
    ResultVO deleteTopicOne(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer)httpServletRequest.getAttribute("id");

        // 1、查找主题
        ProductTopic productTopic = this.productTopicService.getById(id);
        if (productTopic == null) {
            throw new BusinessException(ExceptionEnum.PRODUCT_TOPIC_NOTFOUND);
        }

        // 2.是否有权限删除
        Product product = productService.getById(productTopic.getProductId());
        if (product == null) {
            throw new BusinessException(ExceptionEnum.PRODUCT_TOPIC_DELETED_ERROR);
        }
        if (!userId.equals(product.getUserId())) {
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }

        // 3.删除所有设备的ID
        QueryWrapper<MqttAcl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_topic_id", productTopic.getId());
        this.mqttAclService.remove(queryWrapper);

        // 3.删除
        productTopicService.removeById(id);
        return ResultVO.success();
    }


    @PutMapping("/{id}")
    @CheckLogin
    ResultVO updateTopic(@PathVariable("id") Integer topicId, @RequestBody TopicForm topicForm, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer)httpServletRequest.getAttribute("id");

        ProductTopic topic = this.createTopic(topicForm, userId, true);

        topic.setId(topicId);
        boolean updated = this.productTopicService.updateById(topic);
        if (!updated) {
            throw new BusinessException(ExceptionEnum.PRODUCT_TOPIC_UPDATED_ERROR);
        }

        MqttAcl mqttAcl = new MqttAcl();
        BeanUtils.copyProperties(topic, mqttAcl);
        mqttAcl.setProductTopicId(topic.getId());
        // 给所有更新topic
        QueryWrapper<MqttAcl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_topic_id", topic.getId());
        mqttAclService.list(queryWrapper).stream().forEach(e -> {
            e.setAllow(topicForm.getAllow());
            e.setIpaddr(topicForm.getIpaddr());
            e.setAccess(topicForm.getAccess());
            e.setTopic(topicForm.getTopic());
            e.setTopicDesc(topicForm.getTopicDesc());

            this.mqttAclService.updateById(e);
        });

        return ResultVO.success();
    }

    /**
     * 构造存储对象
     * @param topicForm
     * @param userId
     * @return
     */
    private ProductTopic createTopic(TopicForm topicForm, Integer userId, boolean isUpdate) {
        // 1、权限判断
        Product product = productService.getById(topicForm.getProductId());
        if(!product.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }

        // 拼接主题
        topicForm.setTopic(TopicUtils.createTopic(product.getProductEnName(), topicForm.getTopic()));

        if (!isUpdate) {
            // 判断topic是否已经存在
            QueryWrapper<ProductTopic> queryWrapper = new QueryWrapper();
            queryWrapper.eq("product_id", topicForm.getProductId());
            List<ProductTopic> productTopic = productTopicService.list(queryWrapper);
            productTopic.forEach(e -> {
                if (e.getTopic().equals(topicForm.getTopic())) {
                    throw new BusinessException(ExceptionEnum.PRODUCT_TOPIC_EXISTED_ERROR);
                }
            });
        }

        // 2、添加一条主题
        ProductTopic productTopic = new ProductTopic();
        BeanUtils.copyProperties(topicForm, productTopic);
        productTopic.setOperatorAuth(true);

        return productTopic;
    }

}
