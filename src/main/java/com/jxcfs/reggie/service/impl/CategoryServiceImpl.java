package com.jxcfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.CustomException;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.mapper.CategoryMapper;
import com.jxcfs.reggie.mapper.DishMapper;
import com.jxcfs.reggie.mapper.SetmealMapper;
import com.jxcfs.reggie.pojo.Category;
import com.jxcfs.reggie.pojo.Dish;
import com.jxcfs.reggie.pojo.Employee;
import com.jxcfs.reggie.pojo.Setmeal;
import com.jxcfs.reggie.service.CategoryService;
import com.jxcfs.reggie.service.DishService;
import com.jxcfs.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private DishMapper dishMapper;
    @Resource
    private SetmealMapper setmealMapper;


    /**
     * 分类信息分页
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<Page> pageCategory(Integer page, Integer pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getType).orderByAsc(Category::getSort);
        page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类信息，删除之前要进行判断
     * @param ids
     * @return
     */
    @Override
    public R<String> deleteCategory(Long ids) {

        List<Dish> dishes = dishMapper.selectByCategoryId(ids);
        List<Setmeal> setmeals = setmealMapper.selectByCategoryId(ids);
        System.out.println(dishes);
        System.out.println(setmeals);
        // 判断该分类是否关联菜品
        if(dishes.size() != 0){
            throw new CustomException("该分类已关联菜品，不能删除！");
        }

        // 判断该分类是否关联套餐
        if(setmeals.size() != 0){
            throw new CustomException("该分类已关联套餐，不能删除！");
        }

        categoryMapper.deleteById(ids);
        return R.success("删除成功");
    }

    /**
     * 根据id更新分类信息
     * @param category
     * @return
     */
    @Override
    public R<String> updateCategory(Category category) {
        Category c = categoryMapper.selectById(category.getId());
        category.setType(c.getType());
        updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据type添加分类信息
     * @param category
     * @return
     */
    @Override
    public R<String> insertCategory(Category category) {
        save(category);
        return R.success("添加分类成功");
    }

    /**
     * 根据type查询分类信息
     * @param category
     * @return
     */
    @Override
    public R<List<Category>> list(Category category) {
        if(category.getType() != null ){
            List<Category> categories = categoryMapper.selectByType(category.getType());
            return R.success(categories);
        }
        List<Category> categories = categoryMapper.selectList(null);
        return R.success(categories);

    }


}
