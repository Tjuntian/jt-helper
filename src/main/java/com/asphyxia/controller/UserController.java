package com.asphyxia.controller;


import com.asphyxia.pojo.Code;
import com.asphyxia.pojo.Result;
import com.asphyxia.pojo.User;
import com.asphyxia.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername, username);
        User userOne = userService.getOne(lqw);
        boolean isEmpty = userOne == null ? true : false;
        if (isEmpty) {
            return new Result(Code.GET_ERR, null, "用户不存在");
        }
        if (!userOne.getPassword().equals(password)) {
            return new Result(Code.GET_ERR, null, "账号或密码错误");
        }
        request.getSession().setAttribute("username", userOne.getUsername());
        return new Result(Code.GET_OK, userOne, "登录成功");
    }


    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("username");
        return new Result(Code.GET_OK, null, "退出登录");
    }

    @PostMapping("/register")
    public Result addUser(HttpServletRequest request, @RequestBody User user) {
        String password = user.getPassword();
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername,user.getUsername());
        User one = userService.getOne(lqw);
        if(one!=null){
            return new Result( Code.GET_ERR, null, "用户已存在");
        }
        boolean flag = userService.save(user);
        return new Result(flag ? Code.GET_OK : Code.GET_ERR, null, "注册成功");
    }

}

