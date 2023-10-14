package com.magichell.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.magichell.reggie.entity.Setmeal;
import com.magichell.reggie.entity.SetmealDto;

import java.util.List;

public interface SetmealSetvice extends IService<Setmeal> {
    //新增套餐，同时保存套餐和菜品的关联关系
    public void saveWithDish(SetmealDto setmealDto);

    //删除套餐，同时需要删除套餐和菜品的关联数据
    public void removeWithDish(List<Long> ids);

}
