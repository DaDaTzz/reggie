package com.jxcfs.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 自定义元数据处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
            metaObject.setValue("createTime", LocalDateTime.now());
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("createUser", BaseContext.getCurrent());
            metaObject.setValue("updateUser", BaseContext.getCurrent());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrent());
    }
}
