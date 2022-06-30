package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.domain.form.VenuesForm;
import cn.makerknz.product.server.entity.*;
import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.service.*;
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
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/venues")
@Slf4j
public class VenuesController {

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
    private IThingsLinkageLinkService thingsLinkageLinkService;

    @GetMapping("/list")
    @CheckLogin
    ResultVO getVenuesList(HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        QueryWrapper<Venues> venuesQueryWrapper = new QueryWrapper<>();
        venuesQueryWrapper.eq("user_id", userId);
        List<Venues> venues = venuesService.list(venuesQueryWrapper);

        return ResultVO.success(venues);
    }


    @PostMapping("/add")
    @CheckLogin
    ResultVO addVenues(@RequestBody VenuesForm venuesForm, HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");

        Venues venues = new Venues();
        BeanUtils.copyProperties(venuesForm, venues);
        venues.setUserId(userId);
        boolean saved = venuesService.save(venues);
        if (!saved) {
            throw new BusinessException(ExceptionEnum.VENUES_ADD_ERROR);
        }

        // 添加设备
        venuesForm.getDeviceIds().stream().forEach(e -> {
            Device device = deviceService.getById(e);
            if (device.getUserId().equals(userId)) {
                // 将设备设置到房间中
                VenuesDevice venuesDevice = VenuesDevice.builder()
                        .deviceId(e)
                        .venuesId(venues.getId())
                        .userId(userId)
                        .build();
                this.venuesDeviceService.save(venuesDevice);
            } else {
                log.warn("没有查找到设备!");
            }
        });

        return ResultVO.success(venues);
    }

    @GetMapping("/things/{id}")
    @CheckLogin
    ResultVO getVenuesThings(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
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

        return ResultVO.success(things);
    }



    /**
     * 删除一个场景
     * @param id
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/{id}")
    @CheckLogin
    public ResultVO deleteVenues(@PathVariable("id") Integer id,
                                  HttpServletRequest httpServletRequest) {
        Integer userId = (Integer) httpServletRequest.getAttribute("id");
        Venues venues = this.venuesService.getById(id);
        if (!venues.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.VENUES_THINGS_NOT_FOUND);
        }

        // 移除所有绑定的设备
        QueryWrapper<VenuesDevice> venuesDeviceQueryWrapper = new QueryWrapper<>();
        venuesDeviceQueryWrapper.eq("venues_id", venues.getId());
        boolean remove = this.venuesDeviceService.remove(venuesDeviceQueryWrapper);
//        if (!remove) {
//
//        }

        QueryWrapper<ThingsLinkage> thingsLinkageQueryWrapper = new QueryWrapper<>();
        thingsLinkageQueryWrapper.eq("venues_id", venues.getId());

        // 移除场景联动
        List<ThingsLinkage> thingsLinkages = this.thingsLinkageService.list(thingsLinkageQueryWrapper);
        thingsLinkages.forEach(e -> {
            boolean removeById = this.thingsLinkageService.removeById(id);
            if (removeById) {
                // 删除所有的连接的物
                QueryWrapper thingsLinkageLinkWrapper = new QueryWrapper();
                thingsLinkageLinkWrapper.eq("things_linkage_id", id);
                this.thingsLinkageLinkService.remove(thingsLinkageLinkWrapper);
            }
        });

        this.venuesService.removeById(id);

        return ResultVO.success();
    }

}