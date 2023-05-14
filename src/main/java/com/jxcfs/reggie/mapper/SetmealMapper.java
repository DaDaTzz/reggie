package com.jxcfs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxcfs.reggie.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    List<Setmeal> selectByCategoryId(Long categoryId);
    int updateStatus1ById(String id);
    int updateStatus0ById(String id);
}
