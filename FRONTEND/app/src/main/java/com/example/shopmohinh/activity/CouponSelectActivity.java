package com.example.shopmohinh.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmohinh.R;
import com.example.shopmohinh.adapter.CouponAdapter;
import com.example.shopmohinh.adapter.CouponSelectAdapter;
import com.example.shopmohinh.model.Coupon;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CouponSelectActivity extends AppCompatActivity {
    Button btnPublicCoupon, btnPrivateCoupon;
    RecyclerView recyclerViewCoupon;
    Toolbar toolbar;
    View viewPrivateVoucher, viewPublicVoucher;
    CouponSelectAdapter couponSelectAdapter;
    List<Coupon> couponList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_select);
        initView();
        initControl();
        boolean isPublic = true;
        getCouponApi(isPublic);
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPublicVoucher.setVisibility(View.VISIBLE);
        btnPublicCoupon.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));

        recyclerViewCoupon.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        btnPublicCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearButtonView();
                viewPublicVoucher.setVisibility(View.VISIBLE);
                btnPublicCoupon.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                boolean isPublic = true;
                getCouponApi(isPublic);
            }
        });

        btnPrivateCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearButtonView();
                viewPrivateVoucher.setVisibility(View.VISIBLE);
                btnPrivateCoupon.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                boolean isPublic = false;
                getCouponApi(isPublic);
            }
        });

    }

    private void getCouponApi(boolean isPublic){
        if(!isPublic){
            compositeDisposable.add(apiBanHang.getCoupon(Utils.user_current.getAccount_id())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            couponModel -> {
                                if(couponModel.getResult() != null){
                                    couponList = couponModel.getResult();
                                    couponSelectAdapter = new CouponSelectAdapter(getApplicationContext(),couponList);
                                    recyclerViewCoupon.setAdapter(couponSelectAdapter);
                                    couponSelectAdapter.setOnClickListener(new CouponSelectAdapter.OnClickListener() {
                                        @Override
                                        public void onClick(int position, Coupon coupon) {
                                            Utils.coupon = coupon;
                                            finish();
                                        }
                                    });
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
        } else if (isPublic) {
            compositeDisposable.add(apiBanHang.getCouponPublic()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            couponModel -> {
                                if(couponModel.getResult() != null){
                                    couponList = couponModel.getResult();
                                    couponSelectAdapter = new CouponSelectAdapter(getApplicationContext(),couponList);
                                    recyclerViewCoupon.setAdapter(couponSelectAdapter);
                                    couponSelectAdapter.setOnClickListener(new CouponSelectAdapter.OnClickListener() {
                                        @Override
                                        public void onClick(int position, Coupon coupon) {
                                            Utils.coupon = coupon;
                                            finish();
                                        }
                                    });
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));

        }
    }
    private void clearButtonView() {
        viewPublicVoucher.setVisibility(View.GONE);
        btnPublicCoupon.setTextColor(Color.BLACK);
        viewPrivateVoucher.setVisibility(View.GONE);
        btnPrivateCoupon.setTextColor(Color.BLACK);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarCouponSelect);
        btnPublicCoupon = findViewById(R.id.btnPublicCouponSelect);
        btnPrivateCoupon = findViewById(R.id.btnPrivateCouponSelect);
        recyclerViewCoupon = findViewById(R.id.recyclerViewCouponSelect);
        viewPrivateVoucher = findViewById(R.id.viewPrivateVoucherSelect);
        viewPublicVoucher = findViewById(R.id.viewPublicVoucherSelect);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }
}