package com.jxcfs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxcfs.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    Employee selectByUserName(String name);
}
