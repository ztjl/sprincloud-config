package com.reggie.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.Result;
import com.reggie.entity.Employee;
import com.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employee
     * @return
     */
    @PostMapping("login")
    public Result<Employee> longin(HttpServletRequest request, @RequestBody Employee employee){
        //1.将前端传过来的密码进行加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.没有查询到返回登陆失败
        if (emp == null){
            return Result.error("该用户名不存在");
        }
        //4.密码比对,不成功返回登陆失败
        if (!emp.getPassword().equals(password)){
            return Result.error("密码错误");
        }
        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus()==0){
            return Result.error("该账号已被禁用");
        }

        //6.登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);
    }
    /**
     * 员工退出
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */

    @PostMapping
    public Result<String> add(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息{}",employee.toString());
        //设置初始密码123456，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //获取当前登陆用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return Result.success("新增员工成功");

    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> Page(int page,int pageSize,String name){
        log.info("{},{},{}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getUsername,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee){
        long id = Thread.currentThread().getId();
        log.info("当前线程id为{}",id);

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee !=null){
            return Result.success(employee);
        }
        return Result.error("没有查询到该员工信息");
    }



}
