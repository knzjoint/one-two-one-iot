package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.User;
import cn.makerknz.product.server.mapper.UserMapper;
import cn.makerknz.product.server.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
