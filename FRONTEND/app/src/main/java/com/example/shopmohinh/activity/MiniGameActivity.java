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

import com.airbnb.lottie.LottieAnimationView;
import com.example.shopmohinh.R;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MiniGameActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtCoin, txtLuckyBox;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    LottieAnimationView lbox1, lbox2, lbox3;
    int randLucky = 0;
    int coin = 0, luckyBox = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_game);

        initView();
        actionToolBar();
        initControll();
        getCoin_LuckyBox();
    }

    private void getCoin_LuckyBox() {
        compositeDisposable.add(apiBanHang.getCoin(Utils.user_current.getAccount_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                coin = userModel.getResult().get(0).getCoin();
                                luckyBox = userModel.getResult().get(0).getLuckybox();
                                txtCoin.setText(String.valueOf(coin));
                                txtLuckyBox.setText("Bạn còn " + luckyBox +"/3 lượt");
                            } else {
                                Toast.makeText(getApplicationContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void initControll() {
        lbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (luckyBox > 0){
                    randLucky = new Random().nextInt(3);
                    if (randLucky == 0){
                        showPopup(50, true);
                    } else {
                        showPopup(0, false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đã hết lượt quay của hôm nay.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (luckyBox > 0){
                    randLucky = new Random().nextInt(3);
                    if (randLucky == 1){
                        showPopup(50, true);
                    } else {
                        showPopup(0, false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đã hết lượt quay của hôm nay.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (luckyBox > 0){
                    randLucky = new Random().nextInt(3);
                    if (randLucky == 2){
                        showPopup(50, true);
                    } else {
                        showPopup(0, false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đã hết lượt quay của hôm nay.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showPopup(int coinWin, boolean isWin){
        //Giảm lượt chơi
        luckyBox-=1;
        //Không win
        if (!isWin){
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.lost_luckybox_popup);
            dialog.setCancelable(true);
            dialog.show();

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);

            Button btn = dialog.findViewById(R.id.btnLostLuckyBox);
            btn.setOnClickListener(view -> {
                dialog.cancel();
            });
        } else { //win
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.win_luckybox_popup);
            dialog.setCancelable(true);
            dialog.show();

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);

            TextView winText = dialog.findViewById(R.id.txtWinLuckyBox);
            winText.setText("Bạn nhận được 50 coin");

            //tăng coin
            coin+=50;
            //String discount = points.replace("%", "");
            //insertCoupon("Coupon giảm "+ points +" cho đơn hàng", 1, Integer.parseInt(discount), Utils.user_current.getAccount_id(), 0, 30);
            Button btn = dialog.findViewById(R.id.btnWinLuckyBox);
            btn.setOnClickListener(view -> {
                dialog.cancel();
            });
        }
        updateLuckyBox(Utils.user_current.getAccount_id(), coin, luckyBox);
    }

    private void updateLuckyBox(int id, int coin, int luckyBox) {
        compositeDisposable.add(apiBanHang.updateLuckyBox(id, coin, luckyBox)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                                getCoin_LuckyBox();
                            }
                        },
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

    private void initView(){
        toolbar = findViewById(R.id.toolbarMiniGame);
        lbox1 = findViewById(R.id.luckyBox1);
        lbox2 = findViewById(R.id.luckyBox2);
        lbox3 = findViewById(R.id.luckyBox3);
        txtCoin = findViewById(R.id.txtcoin_luckybox);
        txtLuckyBox = findViewById(R.id.txtSlotLuckyBox);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}