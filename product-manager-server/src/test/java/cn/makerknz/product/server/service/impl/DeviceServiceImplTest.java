package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.domain.param.DeviceParam;
import cn.makerknz.product.server.domain.vo.DeviceVO;
import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.service.IDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.ws.rs.QueryParam;
import java.awt.print.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeviceServiceImplTest {

    @Autowired
    private IDeviceService deviceService;

    @Test
    void selectPage() {
        Page<Device> page = new Page<>(1, 2);
//        Device device = Device.builder()
//                .connectStatus(true)
//                .build();
//        QueryWrapper<Device> queryWrapper = new QueryWrapper<>(device);
//        queryWrapper.eq("connect_status", true);
        DeviceParam deviceParam = new DeviceParam();
//        deviceParam.setConnectStatus(false);
        Page<DeviceVO> deviceVOPage = deviceService.selectPageByWrapper(page, deviceParam);
        Assert.isTrue(deviceVOPage.getRecords().size() != 0, "成");
    }

    @Test
    void findByProductId() {
        deviceService.findByProductId(3).toStream().forEach(e -> System.out.println(e.toString()));
    }
}