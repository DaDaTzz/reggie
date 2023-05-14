package com.jxcfs.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.dto.DishDto;
import com.jxcfs.reggie.pojo.Dish;
import com.jxcfs.reggie.pojo.Setmeal;
import com.jxcfs.reggie.dto.SetmealDto;
import com.jxcfs.reggie.pojo.SetmealDish;
import com.jxcfs.reggie.service.SetmealService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

/**
 * 套餐控制器
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    /**
     * 套餐分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        return setmealService.pageSetmeal(page,pageSize,name);
    }

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        return setmealService.saveSetmeal(setmealDto);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> remove(String[] ids){
        return setmealService.deleteSetmeal(ids);
    }

    /**
     * 启售
     */

    @PostMapping("/status/1")
    public R<String> startSell(String[] ids){
        return setmealService.startSell(ids);
    }

    /**
     * 停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> stopSell(String[] ids){
        return setmealService.stopSell(ids);
    }

    /**
     * 获取套餐信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Setmeal> get(@PathVariable String id){
        return setmealService.getSetmealById(id);
    }


    @GetMapping("/list")
    public R<List<DishDto>> list(Long categoryId, Long status){
        System.out.println(categoryId + "===" + status);
        return setmealService.list(categoryId, status);
    }


}
