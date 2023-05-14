package com.jxcfs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxcfs.reggie.pojo.Dish;
import com.jxcfs.reggie.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    List<Dish> selectByCategoryId(Long categoryId);
    int updateStatus1ById(String id);
    int updateStatus0ById(String id);
    DishDto selectDishDtoByDishId(Long id);
}
