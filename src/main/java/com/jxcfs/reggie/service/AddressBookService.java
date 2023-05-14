package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {

    R<List<AddressBook>> getAddressList();
    R<String> insertAddressBook(AddressBook addressBook);
    R<String> setDefaultAddress(AddressBook addressBook);
    R<AddressBook> getDefaultAddress();
}
