package com.jxcfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.dto.DishDto;
import com.jxcfs.reggie.mapper.*;
import com.jxcfs.reggie.pojo.*;
import com.jxcfs.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishMapper dishMapper;
    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private DishFlavorMapper dishFlavorMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SetmealDishMapper setmealDishMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> pageDish(Integer page,Integer pageSize,String name) {
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        if(name != null){
            queryWrapper.like(Dish::getName,name);
        }

        queryWrapper.orderByDesc(Dish::getUpdateTime);
        page(pageInfo, queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 根据id批量删除菜品
     * @param ids
     * @return
     */
    public R<String> batchDelete(String[] ids){
        ArrayList<String> list1 = new ArrayList<>();
        for (String id : ids) {
            list1.add(id);
        }
        dishMapper.deleteBatchIds(list1);
        // 清空缓存
        Set keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        return R.success("删除成功");
    }

    /**
     * 启售菜品
     * @param ids
     * @return
     */
    @Override
    public R<String> batchStartSell(String[] ids) {
        for (String id : ids) {
            dishMapper.updateStatus1ById(id);
        }
        // 清空缓存
        Set keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        return R.success("启售成功");
    }

    /**
     * 停售菜品
     * @param ids
     * @return
     */
    @Override
    public R<String> batchStopSell(String[] ids) {
        for (String id : ids) {
            dishMapper.updateStatus0ById(id);
        }
        // 清空缓存
        Set keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        return R.success("停售成功");
    }

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @Override
    public R<String> saveDish(DishDto dishDto) {
        save(dishDto);
        Long dishId = dishDto.getId(); // 菜品id
        // 如果添加了口味
        if(dishDto.getFlavors().size() != 0){
            List<DishFlavor> flavors = dishDto.getFlavors();
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorService.saveBatch(flavors);
        }
        redisTemplate.delete(dishDto.getCategoryId());
        return R.success("添加菜品成功");
    }

    /**
     * 根据分类id获取菜品信息
     * @param categoryId
     * @return
     */
    @Override
    public R<List<DishDto>> list(Long categoryId) {
        // 先从redis中获取数据，，
        List<DishDto> list = (List<DishDto>) redisTemplate.opsForValue().get(categoryId.toString());
        // 没有则查询数据库
        if(list == null){
            List<Dish> dishes = dishMapper.selectByCategoryId(categoryId);
            ArrayList<DishDto> dishDtos = new ArrayList<>();
            for (Dish dish : dishes) {
                DishDto dishDto = dishMapper.selectDishDtoByDishId(dish.getId());
                dishDtos.add(dishDto);
            }
            redisTemplate.opsForValue().set(categoryId.toString(), dishDtos,60L, TimeUnit.MINUTES);
            return R.success(dishDtos);
        }
        // 有则直接返回
        return R.success(list);

    }

    /**
     * 根据菜品id获取菜品和口味信息
     * @param id
     * @return
     */
    @Override
    public R<DishDto> getDishWithFlavorById(Long id) {
        DishDto dishDto = dishMapper.selectDishDtoByDishId(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @Override
    public R<String> updateDish(DishDto dishDto) {

        this.updateById(dishDto);

        // 先清理
        dishFlavorMapper.deleteByDishId(dishDto.getId().toString());
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        // 再重新添加菜品口味信息
        dishFlavorService.saveBatch(flavors);
        // 菜品更新后，需要清空缓存
        redisTemplate.delete(dishDto.getCategoryId());
        return R.success("修改成功！");
    }


}
