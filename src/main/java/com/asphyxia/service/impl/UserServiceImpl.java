package com.asphyxia.service.impl;

import com.asphyxia.mapper.UserMapper;
import com.asphyxia.pojo.User;
import com.asphyxia.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author JunTian
 * @since 2023-04-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
