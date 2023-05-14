package com.jxcfs.reggie.controller;

import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.User;
import com.jxcfs.reggie.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user){
        return userService.sendMsg(user);
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map){
        return userService.login(map);
    }
}
