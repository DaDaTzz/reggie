package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.dto.DishDto;
import com.jxcfs.reggie.pojo.Setmeal;
import com.jxcfs.reggie.dto.SetmealDto;
import com.jxcfs.reggie.pojo.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 套餐分页
     * @param page
     * @param pageSize
     * @return
     */
    R<Page> pageSetmeal(Integer page, Integer pageSize,String name);
    R<String> saveSetmeal(SetmealDto setmealDto);
    R<String> deleteSetmeal(String[] ids);
    R<String> startSell(String[] ids);
    R<String> stopSell(String[] ids);
    R<Setmeal> getSetmealById(String id);
    R<List<DishDto>> list(Long categoryId, Long status);
}
