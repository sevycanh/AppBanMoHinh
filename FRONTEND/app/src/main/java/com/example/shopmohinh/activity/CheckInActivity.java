package com.example.shopmohinh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CheckInActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtCoin, txtCheckIn;
    Button btnCheckIn;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    int coin = 0, checkIn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        initView();
        actionToolBar();
        initControll();
        getCoin_CheckIn();
    }

    private void initControll() {
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIn == 1){
                    showPopup();
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đã hết lượt điểm danh của hôm nay.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showPopup(){
        //Giảm lượt chơi
        checkIn-=1;

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.win_coupon_popup);
        dialog.setCancelable(true);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        TextView winText = dialog.findViewById(R.id.txtWinCoupon);
        winText.setText("Bạn nhận được 50 coin");

        //tăng coin
        coin+=50;

        Button btn = dialog.findViewById(R.id.btnWinCoupon);
        btn.setOnClickListener(view -> {
            dialog.cancel();
        });

        updateCheckIn(Utils.user_current.getAccount_id(), coin, checkIn);
    }

    private void updateCheckIn(int id, int coin, int checkIn) {
        compositeDisposable.add(apiBanHang.updateCheckIn(id, coin, checkIn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                                getCoin_CheckIn();
                            }
                        },
                        throwable -> {}
                ));
    }

    private void getCoin_CheckIn() {
        compositeDisposable.add(apiBanHang.getCoin(Utils.user_current.getAccount_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                coin = userModel.getResult().get(0).getCoin();
                                checkIn = userModel.getResult().get(0).getCheck_in();
                                txtCoin.setText(String.valueOf(coin));
                                txtCheckIn.setText("Bạn còn " + checkIn +"/1 lượt điểm danh");
                            } else {
                                Toast.makeText(getApplicationContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
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
        toolbar = findViewById(R.id.toolbarCheckIn);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtCoin = findViewById(R.id.txtcoin_checkin);
        txtCheckIn = findViewById(R.id.txtCheckIn);
        btnCheckIn = findViewById(R.id.btnCheckIn);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}