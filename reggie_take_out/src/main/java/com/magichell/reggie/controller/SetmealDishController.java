package com.magichell.reggie.controller;

import com.magichell.reggie.entity.SetmealDish;
import com.magichell.reggie.service.SetmealDishService;
import com.magichell.reggie.service.SetmealSetvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealDishController {

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealSetvice setmealSetvice;

}
