package com.jxcfs.reggie.service.impl;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.mapper.UserMapper;
import com.jxcfs.reggie.pojo.User;
import com.jxcfs.reggie.service.UserService;
import com.jxcfs.reggie.utils.SMSUtils;
import com.jxcfs.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;


    /**
     * 发送验证码
     * @param user
     * @return
     */
    @Override
    public R<String> sendMsg(User user) {
        String phone = user.getPhone();
        // 随机生成四位验证码
        if(!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 调用阿里云api发送短信验证码
            log.info("code = {}",code);
            //SMSUtils.sendMessage("瑞吉外卖","" , phone,code);
            request.getSession().setAttribute(phone, code);
            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 用户登录
     * @param map
     * @return
     */
    @Override
    public R<User> login(Map map) {
        String phone = map.get("phone").toString();
        if(phone != "" && phone != null){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = this.getOne(queryWrapper);
            //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                this.save(user);
            }
            request.getSession().setAttribute("userId",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
