package com.jxcfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.mapper.AddressBookMapper;
import com.jxcfs.reggie.pojo.AddressBook;
import com.jxcfs.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 地址
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Resource
    private HttpServletRequest request;

    /**
     * 根据用户id查询地址
     * @return
     */
    @Override
    public R<List<AddressBook>> getAddressList() {
        long id =(long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,id).orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBooks = list(queryWrapper);
        return R.success(addressBooks);
    }

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @Override
    public R<String> insertAddressBook(AddressBook addressBook) {
        long id =(long) request.getSession().getAttribute("userId");
        addressBook.setUserId(id);
        save(addressBook);
        return R.success("添加成功");
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @Override
    public R<String> setDefaultAddress(AddressBook addressBook) {
        addressBook.setIsDefault(1);
        updateById(addressBook);
        long id =(long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,id).ne(AddressBook::getId,addressBook.getId());
        addressBook.setIsDefault(0);
        update(addressBook,queryWrapper);
        return R.success("设置默认地址成功");
    }

    /**
     * 获取用户默认收获地址
     * @return
     */
    @Override
    public R<AddressBook> getDefaultAddress() {
        String userId = request.getSession().getAttribute("userId").toString();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId).eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = this.getOne(queryWrapper);
        return R.success(addressBook);
    }
}
