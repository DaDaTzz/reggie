package com.jxcfs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxcfs.reggie.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> selectByType(Integer type);
    int selectDishWithCategoryByid(Long id);
}
