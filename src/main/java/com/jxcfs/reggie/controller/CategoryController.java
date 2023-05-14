package com.jxcfs.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.Category;
import com.jxcfs.reggie.service.CategoryService;
import com.jxcfs.reggie.service.DishService;
import com.jxcfs.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    @Resource
    private DishService dishService;
    @Resource
    private SetmealService setmealService;

    /**
     * 分类信息分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize){
        return categoryService.pageCategory(page, pageSize);
    }

    /**
     * 根据id删除分类信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除id" + ids);
        return categoryService.deleteCategory(ids);
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }


    /**
     * 添加分类信息
     * @param
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        return categoryService.insertCategory(category);
    }


    /**
     * 根据type查询分类信息
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        return categoryService.list(category);
    }

}
