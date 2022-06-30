package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.QrtzCron;
import cn.makerknz.product.server.mapper.QrtzCronMapper;
import cn.makerknz.product.server.service.IQrtzCronService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Cron类型的触发器表 服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2022-04-20
 */
@Service
public class QrtzCronServiceImpl extends ServiceImpl<QrtzCronMapper, QrtzCron> implements IQrtzCronService {

}
