package com.magichell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magichell.reggie.entity.Dish;
import com.magichell.reggie.mapper.DishMapper;
import com.magichell.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{
}
