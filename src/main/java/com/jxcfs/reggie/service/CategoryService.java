package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 分类界面的分页功能
     * @param page
     * @param pageSize
     * @return
     */
    R<Page> pageCategory(Integer page, Integer pageSize);

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    R<String> deleteCategory(Long ids);

    /**
     * 根据id修改分类
     * @param category
     * @return
     */
    R<String> updateCategory(Category category);

    R<String> insertCategory(Category category);

    R<List<Category>> list(Category category);
}
