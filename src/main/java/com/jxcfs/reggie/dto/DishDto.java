package com.jxcfs.reggie.dto;

import com.jxcfs.reggie.pojo.Dish;
import com.jxcfs.reggie.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
