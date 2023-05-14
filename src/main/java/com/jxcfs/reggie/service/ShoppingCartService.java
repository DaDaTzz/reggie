package com.jxcfs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    R<String> add(ShoppingCart shoppingCart);
    R<String> sub(ShoppingCart shoppingCart);
    R<String> clean();
    R<List<ShoppingCart>> getList();

}
