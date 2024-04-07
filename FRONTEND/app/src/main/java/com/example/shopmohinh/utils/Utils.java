package com.example.shopmohinh.utils;

import com.example.shopmohinh.model.Cart;
import com.example.shopmohinh.model.Coupon;
import com.example.shopmohinh.model.Product;
import com.example.shopmohinh.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    //public static final String URLS = "http://192.168.1.8:8019/banmohinh/";
    public  static final String BASE_URL = URLS.ip;

    // public static final String URLS = "http://192.168.43.114:8019/banhang/";

//     public static final String BASE_URL = "http://192.168.1.8:8019/banmohinh/";

    public static User user_current = new User();
    public static List<Cart> carts = new ArrayList<>();
    public static List<Cart> purchases = new ArrayList<>();

    public static Coupon coupon = new Coupon();
    public static Product product = new Product();
    public static String method_payment = "COD";
}