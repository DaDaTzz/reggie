package com.jxcfs.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.Dish;
import com.jxcfs.reggie.dto.DishDto;
import com.jxcfs.reggie.service.DishService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜品控制器
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishService dishService;

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        return dishService.pageDish(page, pageSize,name);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> batchRemove(String[] ids){
        return dishService.batchDelete(ids);
    }

    /**
     * 启售
     * @return
     */
    @PostMapping("/status/1")
    @ResponseBody
    public R<String> batchStartSell(String[] ids){
        return dishService.batchStartSell(ids);
    }

    /**
     * 停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> batchStopSell(String[] ids){
        return dishService.batchStopSell(ids);
    }

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        return dishService.saveDish(dishDto);
    }

    /**
     * 菜品列表
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Long categoryId){
        return dishService.list(categoryId);
    }

    /**
     * 获取菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        return dishService.getDishWithFlavorById(id);
    }

    /**
     * 修改菜品
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        return dishService.updateDish(dishDto);
    }
}
