package com.example.shopmohinh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SpinCouponActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnSpin;
    ImageView imgSpin;
    TextView txtCoin;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    int rotation = 0, rotationSpeed = 10;
    int[] stopPosition ={1824, 1868, 1912, 1956, 2000, 2044, 2088, 2132}; //1824 position ==30% do, 1868 position ==0% tim
    String[] winPoints = {"30%", "0%", "15%", "0%", "5%", "0%", "10%", "0%"};
    int randPosition = 0;
    int coin = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_coupon);

        initView();
        actionToolBar();
        initControll();
        getCoin();
    }

    private void getCoin() {
        compositeDisposable.add(apiBanHang.getCoin(Utils.user_current.getAccount_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                coin = userModel.getResult().get(0).getCoin();
                                txtCoin.setText(String.valueOf(coin));
                            } else {
                                Toast.makeText(getApplicationContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {}
                ));
    }

    private void initControll() {
        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coin >= 500){
                    rotation = 0;
                    rotationSpeed = 10;
                    randPosition = new Random().nextInt(8);
                    //update coin
                    coin -= 500;
                    updateCoin(Utils.user_current.getAccount_id(), coin);

                    startSpin();
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn không đủ coin", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void startSpin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgSpin.setRotation(rotation);

                //control speed spinning
                if (rotation >= 600) {
                    //slow the wheel
                    rotationSpeed = 8;
                }
                if (rotation >= 1000) {
                    //slow the wheel
                    rotationSpeed = 6;
                }
                if (rotation >= 1400) {
                    //slow the wheel
                    rotationSpeed = 4;
                }
                if (rotation >= 1600) {
                    //slow the wheel
                    rotationSpeed = 2;
                }
                rotation += rotationSpeed;
                if (rotation >= stopPosition[randPosition]) {
                    //stop the wheel
                    //Toast.makeText(getApplicationContext(), "Random: "+ String.valueOf(randPosition), Toast.LENGTH_SHORT).show();
                    showPopup(winPoints[randPosition]);
                } else {
                    startSpin();
                }
            }
        }, 1);
    }

    private void updateCoin(int id, int coin) {
        compositeDisposable.add(apiBanHang.updateCoin(id, coin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                                getCoin();
                            }
                        },
                        throwable -> {}
                ));
    }

    public void showPopup(String points){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.win_coupon_popup);
        dialog.setCancelable(true);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        TextView winText = dialog.findViewById(R.id.txtWinCoupon);
        if (points.equals("0%")){
            winText.setText("Chúc bạn may mắn lần sau (T_T)");
        } else {
            winText.setText("Bạn nhận được coupon "+ points+ "\n(HSD: 30 ngày)");
            String discount = points.replace("%", "");
            insertCoupon("Coupon giảm "+ points +" cho đơn hàng", 1, Integer.parseInt(discount), Utils.user_current.getAccount_id(), 0, 30);
        }

        Button btn = dialog.findViewById(R.id.btnWinCoupon);
        btn.setOnClickListener(view -> {
            dialog.cancel();
        });
    }

    private void insertCoupon(String name, int count, int discount, int userId, int isPublic, int duration) {
        compositeDisposable.add(apiBanHang.insertCoupon(name, count, discount, userId, isPublic, duration)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        couponModel -> {},
                        throwable -> {}
                ));
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        btnSpin = findViewById(R.id.btnSpinCoupon);
        imgSpin = findViewById(R.id.imgSpinCoupon);
        toolbar = findViewById(R.id.toolbarSpinCoupon);
        txtCoin = findViewById(R.id.txtcoin_wheelcoupon);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}