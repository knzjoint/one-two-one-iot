package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.domain.form.ApplicationTokenForm;
import cn.makerknz.product.server.entity.ApplicationToken;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.service.IApplicationTokenService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/application-token")
@Slf4j
public class ApplicationTokenController {

    @Autowired
    private IApplicationTokenService applicationTokenService;

    @GetMapping("/page")
    @CheckLogin
    ResultVO getPage(Page page, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        ApplicationToken applicationToken = ApplicationToken.builder()
                .userId(userId)
                .build();

        QueryWrapper<ApplicationToken> applicationTokenQueryWrapper = new QueryWrapper<>(applicationToken);
        Page applicationTokenPage = applicationTokenService.page(page, applicationTokenQueryWrapper);

        return ResultVO.success(applicationTokenPage);
    }

    /**
     * 一个产品可以设置多个应用
     *
     * @param applicationTokenForm
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/add-application")
    @CheckLogin
    ResultVO addApplication(@RequestBody ApplicationTokenForm applicationTokenForm, HttpServletRequest httpServletRequest) throws IOException, PulsarAdminException {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        ApplicationToken applicationToken = new ApplicationToken();
        BeanUtils.copyProperties(applicationTokenForm, applicationToken);
        applicationToken.setUserId(userId);

        return ResultVO.success(applicationTokenService.createPulsarToken(applicationToken));
    }

    /**
     * 删除应用
     *
     * @param id                 设备ID
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/{id}")
    @CheckLogin
    ResultVO deleteApplicationToken(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");
        ApplicationToken applicationToken = this.applicationTokenService.getById(id);
        if (applicationToken == null || !applicationToken.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.USER_PERMISSION_ERROR);
        }

        // 删除设备
        boolean b = this.applicationTokenService.removeById(id);
        if (!b) {
            throw new BusinessException(ExceptionEnum.APPLICATION_DELETED_ERROR);
        }

        return ResultVO.success();
    }

}
