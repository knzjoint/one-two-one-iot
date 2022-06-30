package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.LoginLogs;
import cn.makerknz.product.server.mapper.LoginLogsMapper;
import cn.makerknz.product.server.service.ILoginLogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@Service
public class LoginLogsServiceImpl extends ServiceImpl<LoginLogsMapper, LoginLogs> implements ILoginLogsService {

}
