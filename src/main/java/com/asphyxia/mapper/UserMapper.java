package com.asphyxia.mapper;


import com.asphyxia.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author JunTian
 * @since 2023-04-07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
