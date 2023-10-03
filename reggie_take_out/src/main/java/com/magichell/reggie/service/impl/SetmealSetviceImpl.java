package com.magichell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magichell.reggie.entity.Setmeal;
import com.magichell.reggie.mapper.SetmealMapper;
import com.magichell.reggie.service.SetmealSetvice;
import org.springframework.stereotype.Service;

@Service
public class SetmealSetviceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealSetvice{
}
