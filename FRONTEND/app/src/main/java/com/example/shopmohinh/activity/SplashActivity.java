package com.example.shopmohinh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.shopmohinh.R;
import com.example.shopmohinh.model.Cart;
import com.example.shopmohinh.model.Product;
import com.example.shopmohinh.model.User;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
            initCart();
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3480);
                } catch (Exception e) {
                } finally {
                    if (Paper.book().read("user") == null){
                        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        finish();
                    }
                }
            }
        };
        thread.start();
    }

    private void initCart() {
        Utils.carts.clear();
        compositeDisposable.add(apiBanHang.getShoppingCart(Utils.user_current.getAccount_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                List<Product> productList = productModel.getResult();
                                productToCart(productList);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void productToCart(List<Product> productList) {
        for (int i = 0; i < productList.size(); i++) {
            Product productTemp = productList.get(i);
            Cart cartTemp = new Cart();
            cartTemp.setIdProduct(productTemp.getProduct_id());
            cartTemp.setQuantity(productTemp.getQuantity());
            cartTemp.setName(productTemp.getName());
            int finalPrice = productTemp.getPrice() - (productTemp.getPrice() * productTemp.getCoupon() / 100);
            cartTemp.setPrice(finalPrice);
            cartTemp.setImage(productTemp.getMain_image());
            Utils.carts.add(cartTemp);
            System.out.println(Utils.carts.size());
        }
    }
}