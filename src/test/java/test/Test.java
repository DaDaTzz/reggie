package test;

import com.alibaba.fastjson.JSONObject;
import com.jxcfs.reggie.pojo.Dish;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        String  input  =  "2023-05-13  00:00:00";
        DateTimeFormatter formatter  =  DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
        LocalDateTime  dateTime  =  LocalDateTime.parse(input,  formatter);
        ZonedDateTime utcDateTime  =  dateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime  localDateTime  =  utcDateTime.withZoneSameInstant(ZoneId.systemDefault());
        System.out.println(localDateTime);
    }
}
