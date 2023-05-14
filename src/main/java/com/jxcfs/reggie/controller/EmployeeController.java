package com.jxcfs.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.Employee;
import com.jxcfs.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工控制器
 */
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
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee){
        return employeeService.empLogin(employee);
    }

    /**
     * 员工退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(){
        return employeeService.empLogout();
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee){
        return employeeService.empSave(employee);
    }

    /**
     * 员工分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        return employeeService.empPage(page, pageSize, name);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        return employeeService.empUpdate(employee);
    }

    /**
     * 根据id查找员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        return employeeService.selectById(id);
    }

}
