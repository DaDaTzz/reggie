package com.jxcfs.reggie.dto;


import com.jxcfs.reggie.pojo.Setmeal;
import com.jxcfs.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
