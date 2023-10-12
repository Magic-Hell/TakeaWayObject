package com.magichell.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magichell.reggie.common.CustomException;
import com.magichell.reggie.entity.Dish;
import com.magichell.reggie.entity.DishDto;
import com.magichell.reggie.entity.DishFlavor;
import com.magichell.reggie.entity.Setmeal;
import com.magichell.reggie.mapper.DishMapper;
import com.magichell.reggie.service.DishFlavorService;
import com.magichell.reggie.service.DishService;
import com.magichell.reggie.service.SetmealSetvice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealSetvice setmealSetvice;


    //新增菜品同时保存口味数据
    @Override
    @Transient//事物控制
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品Id

        // 菜品口味
        List<DishFlavor> flavorList = dishDto.getFlavors();

        flavorList = flavorList.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味dishflavor
        dishFlavorService.saveBatch(flavorList);
    }

    //根据id查询菜品信息和对应的口味儿信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询拆品基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表的基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据--dish_flavor表的delect操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

}
