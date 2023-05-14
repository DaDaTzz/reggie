package com.jxcfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.CustomException;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.dto.DishDto;
import com.jxcfs.reggie.dto.SetmealDto;
import com.jxcfs.reggie.mapper.SetmealDishMapper;
import com.jxcfs.reggie.mapper.SetmealMapper;
import com.jxcfs.reggie.pojo.*;
import com.jxcfs.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private SetmealDishMapper setmealDishMapper;
    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private DishService dishService;


    /**
     * 套餐分页
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<Page> pageSetmeal(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        if(name != null){
            queryWrapper.like(Setmeal::getName,name);
        }

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        page(pageInfo, queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @Override
    public R<String> saveSetmeal(SetmealDto setmealDto) {
        save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
        return R.success("添加套餐成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @Override
    public R<String> deleteSetmeal(String[] ids) {
        ArrayList<String> list1 = new ArrayList<>();
        // 先判断要删除的所有套餐中是否还在售卖状态：status = 1
        for (String id : ids) {
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getId, id);
            queryWrapper.eq(Setmeal::getStatus, 1);
            List<Setmeal> list = list(queryWrapper);
            if(list.size() > 0){
                throw new CustomException("删除的套餐还在售卖状态，请先停售！");
            }
        }

        for (String id : ids) {
            list1.add(id);
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId,id);
            setmealDishMapper.delete(queryWrapper);
        }
        setmealMapper.deleteBatchIds(list1);
        return R.success("删除成功！");


    }

    /**
     * 启售
     * @param ids
     * @return
     */
    @Override
    public R<String> startSell(String[] ids) {
        for (String id : ids) {
            setmealMapper.updateStatus1ById(id);
        }
        return R.success("启售成功");
    }

    /**
     * 停售
     * @param ids
     * @return
     */
    @Override
    public R<String> stopSell(String[] ids) {
        for (String id : ids) {
            setmealMapper.updateStatus0ById(id);
        }
        return R.success("停售成功");
    }

    /**
     * 根据id获取套餐信息
     * @param id
     * @return
     */
    @Override
    public R<Setmeal> getSetmealById(String id) {
        Setmeal setmeal = getById(id);
        return R.success(setmeal);
    }

    /**
     *
     * @param categoryId
     * @param status
     * @return
     */
    @Override
    public R<List<DishDto>> list(Long categoryId, Long status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId);
        Setmeal setmeal = this.getOne(queryWrapper);
        Long setmealId = setmeal.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(SetmealDish::getSetmealId, setmealId);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper1);
        ArrayList<DishDto> dishDtos = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes) {
            Long dishId = setmealDish.getDishId();
            LambdaQueryWrapper<DishFlavor> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> list = dishFlavorService.list(queryWrapper2);
            DishDto dishDto = new DishDto();
            dishDto.setId(setmealDish.getDishId());
            dishDto.setPrice(setmealDish.getPrice());
            dishDto.setFlavors(list);
            dishDto.setName(setmealDish.getName());
            dishDtos.add(dishDto);
            LambdaQueryWrapper<Dish> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(Dish::getId,dishId);
            Dish d = dishService.getOne(queryWrapper3);
            dishDto.setImage(d.getImage());
        }
        return R.success(dishDtos);
    }


}
