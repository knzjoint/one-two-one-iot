package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.domain.control.*;
import cn.makerknz.product.server.domain.form.ControlDeviceForm;
import cn.makerknz.product.server.domain.form.ThingsForm;
import cn.makerknz.product.server.domain.format.*;
import cn.makerknz.product.server.emqx.EmqxAdminService;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.entity.Product;
import cn.makerknz.product.server.entity.Things;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.rule.actions.ThingsControlActions;
import cn.makerknz.product.server.service.IDeviceService;
import cn.makerknz.product.server.service.IProductService;
import cn.makerknz.product.server.service.IThingsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/things")
public class ThingsController {

    @Autowired
    private IThingsService thingsService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IProductService productService;

    @Autowired
    private EmqxAdminService emqxAdminService;

    @Autowired
    private Gson gson;

    // TODO 增加单位

    /**
     * 设备下面的物列表
     *
     * @param clientId
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/list/{id}")
    @CheckLogin
    ResultVO getClientThings(@PathVariable("id") String clientId, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        QueryWrapper<Things> thingsQueryWrapper = new QueryWrapper<>();
        thingsQueryWrapper.eq("client_id", clientId);
        thingsQueryWrapper.eq("user_id", userId);
        List<Things> thingsList = thingsService.list(thingsQueryWrapper);

        return ResultVO.success(thingsList);
    }


    /**
     * 设备下面的物列表
     *
     * @param thingsForm
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/add")
    @CheckLogin
    ResultVO addThings(@RequestBody ThingsForm thingsForm, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        // 查找设备的权限
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("user_id", userId);
        deviceQueryWrapper.eq("client_id", thingsForm.getClientId());
        Device deviceServiceOne = deviceService.getOne(deviceQueryWrapper);
        if (deviceServiceOne == null) {
            throw new BusinessException(ExceptionEnum.DEVICE_EXISTED_ERROR);
        }

        // 构造物模型
        Things things = new Things();
        BeanUtils.copyProperties(thingsForm, things);
        String controlUi = "{}";
        // 修改
        if (ControlEnum.BUTTON.equals(thingsForm.getControlType())) {
            Button button = new Button();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), button);
            button.setControlType(ControlEnum.BUTTON);
            controlUi = gson.toJson(button);
        } else if (ControlEnum.INPUT.equals(thingsForm.getControlType())) {
            Input input = new Input();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), input);
            input.setControlType(ControlEnum.INPUT);
            controlUi = gson.toJson(input);
        } else if (ControlEnum.SLIDER.equals(thingsForm.getControlType())) {
            Slider slider = new Slider();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), slider);
            slider.setControlType(ControlEnum.SLIDER);
            controlUi = gson.toJson(slider);
        } else if (ControlEnum.SWITCH.equals(thingsForm.getControlType())) {
            Switch aSwitch = new Switch();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), aSwitch);
            aSwitch.setControlType(ControlEnum.SWITCH);
            controlUi = gson.toJson(aSwitch);
        } else if (ControlEnum.TEXT_INPUT.equals(thingsForm.getControlType())) {
            TextInput textInput = new TextInput();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), textInput);
            textInput.setControlType(ControlEnum.TEXT_INPUT);
            controlUi = gson.toJson(textInput);
        } else if (ControlEnum.JSON_INPUT.equals(thingsForm.getControlType())) {
            JsonInput jsonInput = new JsonInput();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), jsonInput);
            jsonInput.setControlType(ControlEnum.JSON_INPUT);
            controlUi = gson.toJson(jsonInput);
        }

        things.setControlUi(controlUi);


        // DataFormat
        String dateFormat = "{}";
        if (thingsForm.getDataTypeId() == 1) {
            IntDataFormat intDataFormat = new IntDataFormat();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), intDataFormat);
            intDataFormat.setType(DataTypeEnum.INT);
            dateFormat = gson.toJson(intDataFormat);

            things.setThingsType(DataTypeEnum.INT);
            things.setThingsValue(intDataFormat.getMin().toString());
        } else if (thingsForm.getDataTypeId() == 2) {
            DecimalDataFormat decimalDataFormat = new DecimalDataFormat();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), decimalDataFormat);
            decimalDataFormat.setType(DataTypeEnum.DECIMAL);
            dateFormat = gson.toJson(decimalDataFormat);

            things.setThingsType(DataTypeEnum.DECIMAL);
            things.setThingsValue(String.valueOf(decimalDataFormat.getMin()));
        } else if (thingsForm.getDataTypeId() == 3) {
            StringDataFormat stringDataFormat = new StringDataFormat();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), stringDataFormat);
            stringDataFormat.setType(DataTypeEnum.STRING);
            dateFormat = gson.toJson(stringDataFormat);

            things.setThingsType(DataTypeEnum.STRING);
            things.setThingsValue("");
        } else if (thingsForm.getDataTypeId() == 4) {
            BoolDataFormat boolDataFormat = new BoolDataFormat();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), boolDataFormat);
            boolDataFormat.setType(DataTypeEnum.BOOL);
            dateFormat = gson.toJson(boolDataFormat);

            things.setThingsType(DataTypeEnum.BOOL);
            things.setThingsValue("true");
        } else if (thingsForm.getDataTypeId() == 5) {
            JsonDataFormat jsonDataFormat = new JsonDataFormat();
            BeanUtils.copyProperties(thingsForm.getDataFormat(), jsonDataFormat);
            jsonDataFormat.setType(DataTypeEnum.JSON);
            dateFormat = gson.toJson(jsonDataFormat);

            things.setThingsType(DataTypeEnum.JSON);
            things.setThingsValue("{}");
        }
        things.setDataFormat(dateFormat);

        // 查找相同的内容增加序列号
        QueryWrapper<Things> thingsQueryWrapper = new QueryWrapper<>();
        thingsQueryWrapper.eq("client_id", thingsForm.getClientId());
        thingsQueryWrapper.eq("things_name", thingsForm.getThingsName());
        List<Things> thingsList = thingsService.list(thingsQueryWrapper);
        if (thingsList.size() == 0) {
            things.setSequence(1);
        } else {
            Things thingsMax = thingsList.stream().max(Comparator.comparing(Things::getSequence)).get();
            things.setSequence(thingsMax.getSequence() + 1);
        }

        things.setUserId(userId);
        // 判断是否存在icon
        if (thingsForm.getThingsIcon() == null) {
            things.setThingsIcon("http://image.makerknz.cn/things/pictureSplit.svg");
        }
        boolean save = thingsService.save(things);
        if (!save) {
            throw new BusinessException(ExceptionEnum.THINGS_ADD_ERROR);
        }

        return ResultVO.success();
    }


    @PostMapping("/control-things")
    @CheckLogin
    ResultVO getControlThings(@RequestBody ControlDeviceForm controlDeviceForm, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");
        Things things = thingsService.getById(controlDeviceForm.getThingsId());
        if (things == null || !things.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.THINGS_USER_NO_PERMISSION);
        }

//        ThingsServiceImpl.controlThings(things.getId(), controlDeviceForm);
        ThingsControlActions.controlThings(things.getId(), controlDeviceForm);

        return ResultVO.success();
    }


    @DeleteMapping("/{id}")
    @CheckLogin
    ResultVO deleteThings(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");
        Things things = thingsService.getById(id);
        if (things == null || !things.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.THINGS_USER_NO_PERMISSION);
        }

        // topic 构造
        this.thingsService.removeById(id);

        return ResultVO.success();
    }


    @GetMapping("/copy-topic/{id}")
    @CheckLogin
    ResultVO copyThingsTopic(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");
        Things things = thingsService.getById(id);
        if (things == null || !things.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.THINGS_USER_NO_PERMISSION);
        }

        // topic 构造
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("client_id", things.getClientId());
        Device device = this.deviceService.getOne(deviceQueryWrapper);
        if (device == null) {
            throw new BusinessException(ExceptionEnum.THINGS_USER_NO_PERMISSION);
        }
        Product product = this.productService.getById(device.getProductId());
        String topic = "/" + product.getProductEnName() + "/" + device.getClientId() + "/things/" + things.getThingsName() + "/" + things.getSequence();

        return ResultVO.success(topic);
    }

}