package com.jxcfs.reggie.controller;

import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.AddressBook;
import com.jxcfs.reggie.service.AddressBookService;
import org.apache.tomcat.jni.Address;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Resource
    private AddressBookService addressBookService;


    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        return addressBookService.getAddressList();
    }

    @PostMapping
    public R<String> insertAddress(@RequestBody AddressBook addressBook){
        return addressBookService.insertAddressBook(addressBook);
    }

    @PutMapping("/default")
    public R<String> defaultAddress(@RequestBody AddressBook addressBook){
        return addressBookService.setDefaultAddress(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(){
        return addressBookService.getDefaultAddress();
    }

}
