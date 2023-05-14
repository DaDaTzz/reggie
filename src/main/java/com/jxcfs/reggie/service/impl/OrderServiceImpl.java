package com.jxcfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.mapper.OrderMapper;
import com.jxcfs.reggie.pojo.*;
import com.jxcfs.reggie.service.*;
import com.jxcfs.reggie.utils.ValidateCodeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Resource
    private HttpServletRequest request;
    @Resource
    private AddressBookService addressBookService;
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private DishService dishService;
    @Resource
    private UserService userService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 生成订单
     * @param orders
     * @return
     */
    @Override
    public R<String> submit(Orders orders) {
        // 订单编号
        String number = ValidateCodeUtils.generateValidateCode(6).toString();
        orders.setNumber(number);
        // 用户id
        long userId =(long) request.getSession().getAttribute("userId");
        orders.setUserId(userId);
        // 默认收获地址
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId).eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        Long addressBookId = addressBook.getId();
        String address = addressBook.getDetail();
        if(address == null || address == ""){
            throw new RuntimeException("没有默认收获地址，不能下单！");
        }
        orders.setAddressBookId(addressBookId);
        orders.setAddress(address);
        // 下单时间
        orders.setOrderTime(LocalDateTime.now());
        // 支付时间
        orders.setCheckoutTime(LocalDateTime.now());
        // 总金额
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        ArrayList<Long> dishids = new ArrayList<Long>();
        for (ShoppingCart shoppingCart : list) {
            Long dishId = shoppingCart.getDishId();
            dishids.add(dishId);
        }
        List<Dish> dishes = dishService.listByIds(dishids);
        BigDecimal price = new BigDecimal("0");
        for (Dish dish : dishes) {
            BigDecimal p = new BigDecimal(dish.getPrice().toString());
            price = price.add(p);
        }
        orders.setAmount(price);
        // 电话
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<User>();
        userLambdaQueryWrapper.eq(User::getId,userId);
        User user = userService.getOne(userLambdaQueryWrapper);
        orders.setPhone(user.getPhone());
        // 姓名
        orders.setUserName(user.getName());
        orders.setConsignee(user.getName());
        orders.setStatus(2);
        this.save(orders);
        Long orderId = orders.getId();

        // 订单明细表，插入多条数据
        for (Dish dish : dishes) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(dish.getName());
            orderDetail.setImage(dish.getImage());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(dish.getId());
            orderDetail.setAmount(dish.getPrice());
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getDishId,dish);
            List<SetmealDish> list1 = setmealDishService.list(setmealDishLambdaQueryWrapper);
            if(list1.size() != 0){
                for (SetmealDish setmealDish : list1) {
                    orderDetail.setSetmealId(setmealDish.getId());
                }
            }
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dish.getId());
            List<DishFlavor> list2 = dishFlavorService.list(queryWrapper1);
            if(list2.size() != 0){
                for (DishFlavor dishFlavor : list2) {
                    orderDetail.setDishFlavor(dishFlavor.getValue());
                }
            }
            String number1 = orders.getNumber();
            orderDetail.setNumber(Integer.parseInt(number1));
            // 保存订单明细
            orderDetailService.save(orderDetail);
        }

        // 清空购物车
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("支付成功！");
    }

    /**
     * 用户订单分页
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<Page> page(Long page, Long pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders::getStatus);
        page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 管理端订单分页
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public R<Page> pageInAdmin(Long page, Long pageSize,String number,String beginTime,String endTime) throws ParseException {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if(number != null){
            queryWrapper.like(Orders::getNumber,number);
        }
        if(beginTime != null && endTime != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime b = LocalDateTime.parse(beginTime, formatter);
            LocalDateTime e = LocalDateTime.parse(endTime, formatter);
            queryWrapper.between(Orders::getOrderTime,b,e);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 订单派送
     * @param orders
     * @return
     */
    @Override
    public R<String> handOut(Orders orders) {
        this.updateById(orders);
        return R.success("派送成功！");
    }


}
