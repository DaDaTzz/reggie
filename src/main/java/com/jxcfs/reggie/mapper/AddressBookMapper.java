package com.jxcfs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
