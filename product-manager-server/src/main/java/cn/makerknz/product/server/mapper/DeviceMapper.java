package cn.makerknz.product.server.mapper;

import cn.makerknz.product.server.domain.vo.DeviceVO;
import cn.makerknz.product.server.entity.Device;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
public interface DeviceMapper extends BaseMapper<Device> {

    Page<DeviceVO> selectPageByWrapper(Page<Device> page, @Param(Constants.WRAPPER) Wrapper<Device> queryWrapper);

}
