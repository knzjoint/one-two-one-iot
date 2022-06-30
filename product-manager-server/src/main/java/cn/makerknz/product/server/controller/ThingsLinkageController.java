package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.domain.form.ThingsLinkageForm;
import cn.makerknz.product.server.domain.param.ThingsLinkageParam;
import cn.makerknz.product.server.entity.*;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.rule.WorkFlowUtils;
import cn.makerknz.product.server.rule.core.domain.EndNode;
import cn.makerknz.product.server.rule.core.domain.ThingsNode;
import cn.makerknz.product.server.rule.core.domain.WorkFlowForm;
import cn.makerknz.product.server.rule.core.domain.WorkFlowNodes;
import cn.makerknz.product.server.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/things-linkage")
public class ThingsLinkageController {

    @Autowired
    private IVenuesService venuesService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IVenuesDeviceService venuesDeviceService;

    @Autowired
    private IThingsService thingsService;

    @Autowired
    private IThingsLinkageService thingsLinkageService;

    @Autowired
    private Gson gson;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IThingsLinkageLinkService thingsLinkageLinkService;

    /**
     * 添加场景联动规则
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/add")
    @CheckLogin
    public ResultVO addRules(@RequestBody ThingsLinkageForm thingsLinkageForm,
                             HttpServletRequest httpServletRequest) throws JsonProcessingException {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        Venues venues = this.venuesService.getById(thingsLinkageForm.getVenuesId());
        if (venues == null || !venues.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.THINGS_LINKAGE_PERMISSION_ERROR);
        }

        ThingsLinkage thingsLinkage = new ThingsLinkage();
        BeanUtils.copyProperties(thingsLinkageForm, thingsLinkage);
        thingsLinkage.setUserId(userId);
        thingsLinkage.setControlUi("[{\"id\":\"startNode\",\"uuid\":\"13c81280-0d16-46c8-ba8b-3d54b65bd63e\",\"name\":\"start\",\"type\":\"startNode\",\"groupType\":\"flow\",\"hidden\":true,\"left\":388.0,\"top\":30.0,\"className\":\"step-start\",\"next\":[\"16934c89-f716-4501-84bd-0accd986b725\"],\"prev\":[],\"formData\":{}},{\"id\":\"tempNode\",\"uuid\":\"16934c89-f716-4501-84bd-0accd986b725\",\"name\":\"temp\",\"type\":\"tempNode\",\"groupType\":\"temp\",\"left\":388.0,\"top\":150.0,\"className\":\"step-temp\",\"next\":[],\"prev\":[\"13c81280-0d16-46c8-ba8b-3d54b65bd63e\"],\"formData\":{}}]\n"); // 设置一个默认的UI

        boolean save = this.thingsLinkageService.save(thingsLinkage);
        if (!save) {
            throw new BusinessException(ExceptionEnum.THINGS_LINKAGE_PERMISSION_ERROR);
        }

        return ResultVO.success(thingsLinkage);
    }


    /**
     *
     * @param id  指的是things linkage的id
     * @param workFlowForms
     * @param httpServletRequest
     * @return
     */
    @PutMapping("/update-rules/{id}")
    @CheckLogin
    public ResultVO updateRules(@PathVariable("id") Integer id,
                                @RequestBody List<WorkFlowForm> workFlowForms,
                                HttpServletRequest httpServletRequest) throws JsonProcessingException {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        ThingsLinkage thingsLinkage = thingsLinkageService.getById(id);
        if (!thingsLinkage.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.THINGS_LINKAGE_PERMISSION_ERROR);
        }

        String uiJson = objectMapper.writeValueAsString(workFlowForms);

        String rule = WorkFlowUtils.workFlowUiToRule(workFlowForms);

        // 设置UI和规则
        thingsLinkage.setControlUi(uiJson);
        thingsLinkage.setRule(rule);
        this.thingsLinkageService.updateById(thingsLinkage);

        thingsLinkageLinkService.saveAllThingsLinkageLink(id, workFlowForms);

        return ResultVO.success();
    }

    /**
     *
     * @param id  指的是things linkage的id
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/{id}")
    @CheckLogin
    public ResultVO getThingsLinkage(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        ThingsLinkage thingsLinkage = thingsLinkageService.getById(id);
        if (!thingsLinkage.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.THINGS_LINKAGE_PERMISSION_ERROR);
        }

        return ResultVO.success(thingsLinkage);
    }


    /**
     *
     * @param thingsLinkageParam  指的是venuesId
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/page")
    @CheckLogin
    public ResultVO getVenuesThingsLinkage(Page page,
                                           ThingsLinkageParam thingsLinkageParam,
                                           HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        ThingsLinkage thingsLinkage = new ThingsLinkage();
        BeanUtils.copyProperties(thingsLinkageParam, thingsLinkage);

        // 获取列表
        QueryWrapper<ThingsLinkage> queryWrapper = new QueryWrapper(thingsLinkage);
        queryWrapper.eq("user_id", userId);

        Page thingsLinkages = this.thingsLinkageService.page(page, queryWrapper);

        return ResultVO.success(thingsLinkages);
    }


    @GetMapping("/work-flow/{id}")
    @CheckLogin
    public ResultVO getWorkFlowNode(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        // 查看venues是否存在
        Venues venues = this.venuesService.getById(id);
        if (venues == null || !venues.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.VENUES_THINGS_PERMISSION_ERROR);
        }

        // 查找设备列表
        QueryWrapper<VenuesDevice> venuesDeviceQueryWrapper = new QueryWrapper<>();
        venuesDeviceQueryWrapper.eq("user_id", userId);
        venuesDeviceQueryWrapper.eq("venues_id", venues.getId());
        List<VenuesDevice> venuesDeviceList = this.venuesDeviceService.list(venuesDeviceQueryWrapper);
        if (venuesDeviceList.size() == 0) {
            throw new BusinessException(ExceptionEnum.VENUES_THINGS_NOT_FOUND);
        }

        List<Integer> deviceIds = venuesDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.in("id", deviceIds);
        List<Device> devices = deviceService.list(deviceQueryWrapper);
        if (devices.size() == 0) {
            throw new BusinessException(ExceptionEnum.VENUES_THINGS_NOT_FOUND);
        }

        // 获取things
        List<String> clientIds = devices.stream().map(e -> e.getClientId()).collect(Collectors.toList());
        QueryWrapper<Things> thingsQueryWrapper = new QueryWrapper<>();
        thingsQueryWrapper.in("client_id", clientIds);
        List<Things> things = this.thingsService.list(thingsQueryWrapper);

        // thingsNode
        List<ThingsNode> thingsNodes = things.stream().map(e -> {
            return WorkFlowUtils.createThingsNode(e);
        }).collect(Collectors.toList());

        List<EndNode> endNodes = things.stream().map(e -> {
            return WorkFlowUtils.createFlowNode(e);
        }).collect(Collectors.toList());

        WorkFlowNodes workFlowNodes = WorkFlowNodes.builder()
                .input(thingsNodes)
                .flow(endNodes)
                .build();

        return ResultVO.success(workFlowNodes);

    }

    /**
     * 删除一个联动场景
     * @param id
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/{id}")
    @CheckLogin
    public ResultVO deleteLinkage(@PathVariable("id") Integer id,
                                  HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");
        ThingsLinkage thingsLinkage = this.thingsLinkageService.getById(id);
        if (!thingsLinkage.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.VENUES_THINGS_NOT_FOUND);
        }

        boolean removeById = this.thingsLinkageService.removeById(id);
        if (removeById) {
            // 删除所有的连接的物
            QueryWrapper thingsLinkageLinkWrapper = new QueryWrapper();
            thingsLinkageLinkWrapper.eq("things_linkage_id", id);
            this.thingsLinkageLinkService.remove(thingsLinkageLinkWrapper);
        }

        return ResultVO.success();
    }

}
