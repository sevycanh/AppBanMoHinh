package com.example.shopmohinh.utils;

import com.example.shopmohinh.model.User;

public class Utils {
    //wifi nha canh: http://192.168.1.8:8019/banmohinh/
    public static final String BASE_URL = "http://192.168.1.8:8019/banmohinh/";

    //wifi dien thoai phat canh
//    public static final String BASE_URL = "http://192.168.43.114:8019/banhang/";
    public static User user_current = new User();

    public static List<Cart> carts = new ArrayList<>();
    public static List<Cart> purchases = new ArrayList<>();
}
