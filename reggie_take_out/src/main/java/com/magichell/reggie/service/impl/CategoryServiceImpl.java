package com.magichell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magichell.reggie.entity.Category;
import com.magichell.reggie.mapper.CategoryMapper;
import com.magichell.reggie.service.CategoryService;
import com.magichell.reggie.service.DishService;
import com.magichell.reggie.service.SetmealSetvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealSetvice setmealSetvice;

    @Override
    public void remove(Long id) {

        /**
         * 查询当前分类是否关联了菜品，如果已经关联，
         * 抛出一个业务异常
         */


        /**
         * 查询当前分类是否关联了套餐，如果已经关联，
         * 抛出一个业务异常
         */

        //正常删除餐品

    }
}
