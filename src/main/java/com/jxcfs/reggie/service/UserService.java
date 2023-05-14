package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.User;

import java.util.Map;

public interface UserService extends IService<User> {
    R<String> sendMsg(User user);
    R<User> login(Map map);
}
