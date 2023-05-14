package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.Dish;
import com.jxcfs.reggie.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {

    R<Page> pageDish(Integer page,Integer pageSize,String name);
    R<String> batchDelete(String[] ids);
    R<String> batchStartSell(String[] ids);
    R<String> batchStopSell(String[] ids);
    R<String> saveDish(DishDto dishDto);
    R<List<DishDto>> list(Long categoryId);
    R<DishDto> getDishWithFlavorById(Long id);
    R<String> updateDish(DishDto dishDto);
}
