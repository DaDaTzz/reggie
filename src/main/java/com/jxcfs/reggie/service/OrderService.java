package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.Orders;

import java.text.ParseException;
import java.time.LocalDateTime;

public interface OrderService extends IService<Orders> {
    R<String> submit(Orders orders);
    R<Page> page(Long page,Long pageSize);
    R<Page> pageInAdmin(Long page, Long pageSize, String number, String beginTime, String endTime) throws ParseException;
    R<String> handOut(Orders orders);
}
