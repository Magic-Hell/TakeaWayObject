package com.magichell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.magichell.reggie.common.R;
import com.magichell.reggie.entity.Employee;
import com.magichell.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //员工登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request , @RequestBody Employee employee){

        /**
         * 1. 将页面提交的密码Password进行MD5加密处理
         * 2. 根据页面提交的用户名username查询数据库
         * 3. 如果没有查询到则返回登录失败结果
         * 4. 密码对比，如果不一致则返回登录失败的结果
         * 5. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
         * 6. 登录成功，将员工ID存入Session并返回登录成功结果
         */

        //将页面提交的密码Password进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //如果没有查询到则返回登录失败结果
        if (emp == null){
            return  R.error("登录失败");
        }

        //密码对比，如果不一致则返回登录失败的结果
        if (!emp.getPassword().equals(password)){
            return  R.error("登录失败");
        }

        //查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            return  R.error("账号已禁用");
        }

        //登录成功，将员工ID存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

}