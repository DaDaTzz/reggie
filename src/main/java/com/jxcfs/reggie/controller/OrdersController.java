package com.jxcfs.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.OrderDetail;
import com.jxcfs.reggie.pojo.Orders;
import com.jxcfs.reggie.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Resource
    private OrderService orderService;

    /**
     * 生成订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(Orders orders){
        return orderService.submit(orders);
    }

    /**
     * 用户端订单分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(Long page,Long pageSize){
        return orderService.page(page, pageSize);
    }

    /**
     * 管理端订单分页
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageInAdmin(Long page, Long pageSize, String number, String beginTime,String endTime){
        try {
            return orderService.pageInAdmin(page, pageSize, number,beginTime,endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 订单派送
     * @param orders
     * @return
     */
    @PutMapping
    private R<String> handOut(@RequestBody Orders orders){
        return orderService.handOut(orders);
    }


}
