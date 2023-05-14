package com.jxcfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.mapper.EmployeeMapper;
import com.jxcfs.reggie.pojo.Employee;
import com.jxcfs.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;
    @Autowired
    private HttpServletRequest request;

    /**
     * 员工登录
     * @param employee
     * @return
     */
    @Override
    public R<Employee> empLogin(Employee employee) {
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        Employee emp = employeeMapper.selectByUserName(employee.getUsername());
        if(emp == null){
            return R.error("用户不存在");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        if(emp.getStatus() != 1){
            return R.error("用户已禁用");
        }

        request.getSession().setAttribute("empId", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @return
     */
    @Override
    public R<String> empLogout() {
        request.getSession().removeAttribute("empId");
        return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @Override
    public R<String> empSave(Employee employee) {
        Employee e = employeeMapper.selectByUserName(employee.getUsername());
        if(e != null){
           return R.error("用户名已存在");
        }

        // 密码
        employee.setPassword(DigestUtils.md5DigestAsHex("666666".getBytes()));
        // 更新和创建的用户
        long empId = (long) request.getSession().getAttribute("empId");

        employee.setUpdateUser(empId);
        employee.setCreateUser(empId);
        // 更新和创建的时间
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateTime(LocalDateTime.now());

        save(employee);
        return R.success("添加成功");
    }

    /**
     * 员工分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> empPage(Integer page, Integer pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if(name != null){
            queryWrapper.like(Employee::getName,name);
        }
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @Override
    public R<String> empUpdate(Employee employee) {
        employeeMapper.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查找员工信息
     * @param ids
     * @return
     */
    @Override
    public R<Employee> selectById(Long ids) {
        Employee employee = employeeMapper.selectById(ids);
        return R.success(employee);
    }

}
