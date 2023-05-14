package com.jxcfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.mapper.ShoppingCartMapper;
import com.jxcfs.reggie.pojo.Dish;
import com.jxcfs.reggie.pojo.SetmealDish;
import com.jxcfs.reggie.pojo.ShoppingCart;
import com.jxcfs.reggie.service.DishService;
import com.jxcfs.reggie.service.SetmealDishService;
import com.jxcfs.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Resource
    private HttpServletRequest request;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private DishService dishService;


    /**
     * 加入购物车
     * @param shoppingCart
     * @return
     */
    @Override
    public R<String> add(ShoppingCart shoppingCart) {
        // 获取用户id
        long userId =(long) request.getSession().getAttribute("userId");
        shoppingCart.setUserId(userId);
        // 获取setemalId
        Long setmealId = null;
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId,shoppingCart.getDishId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        if (list != null) {
            for (SetmealDish setmealDish : list) {
                setmealId = setmealDish.getSetmealId();
            }
            shoppingCart.setSetmealId(setmealId);
        }

        // 菜品金额
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Dish::getId,shoppingCart.getDishId());
        Dish dish = dishService.getOne(queryWrapper1);
        shoppingCart.setAmount(dish.getPrice());
        // 时间
        LocalDateTime now = LocalDateTime.now();
        shoppingCart.setCreateTime(now);

        this.save(shoppingCart);
        return R.success("添加成功");
    }

    /**
     * 移除购物车
     * @param shoppingCart
     * @return
     */
    @Override
    public R<String> sub(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        remove(shoppingCartLambdaQueryWrapper);
        return R.success("移除成功");
    }

    /**
     * 根据用户id清空购物车
     * @return
     */
    @Override
    public R<String> clean() {
        String userId = request.getSession().getAttribute("userId").toString();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        remove(shoppingCartLambdaQueryWrapper);
        return R.success("已清空购物车");
    }


    /**
     * 根据用户id获取购物车信息
     * @return
     */
    @Override
    public R<List<ShoppingCart>> getList() {
        String userId = request.getSession().getAttribute("userId").toString();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = list(queryWrapper);
        return R.success(shoppingCarts);
    }
}
