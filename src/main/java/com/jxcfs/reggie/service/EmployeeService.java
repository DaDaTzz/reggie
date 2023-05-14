package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    /**
     * 员工登录
     * @param employee
     * @return
     */
    R<Employee> empLogin(Employee employee);

    /**
     * 员工退出
     * @return
     */
    R<String> empLogout();

    R<String> empSave(Employee employee);
    R<Page> empPage(Integer page,Integer pageSize,String name);

    R<String> empUpdate(Employee employee);
    R<Employee> selectById(Long id);
}
