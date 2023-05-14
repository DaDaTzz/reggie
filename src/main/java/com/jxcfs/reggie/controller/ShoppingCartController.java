package com.jxcfs.reggie.controller;

import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.pojo.ShoppingCart;
import com.jxcfs.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        return shoppingCartService.getList();
    }

    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.add(shoppingCart);
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.sub(shoppingCart);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        return shoppingCartService.clean();
    }
}
