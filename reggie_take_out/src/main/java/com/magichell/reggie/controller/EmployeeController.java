package com.magichell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.magichell.reggie.common.R;
import com.magichell.reggie.entity.Employee;
import com.magichell.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired    private EmployeeService employeeService;

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

    //员工退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    //员工添加
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工,员工信息:{}",employee.toString());
        //设置初始密码，需要进行MD%加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //获取当前登录用户的id
//        Long empID = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    //员工分页信息查询
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        //构造分页构造起
        Page pageInfo = new Page (page,pageSize);
        //构造条件构造起
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update (HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    //根据id来查询员工信息
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
