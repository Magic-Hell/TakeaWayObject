package com.magichell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magichell.reggie.entity.Employee;
import com.magichell.reggie.mapper.EmployeeMapper;
import com.magichell.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {



}
