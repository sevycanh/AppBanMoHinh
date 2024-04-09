package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.model.NotiSendData;
import com.manager.appbanmohinhmanager.model.User;
import com.manager.appbanmohinhmanager.retrofit.ApiBanHang;
import com.manager.appbanmohinhmanager.retrofit.ApiPushNotification;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClientNoti;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    MaterialCardView cardViewProductManager, cardViewCategoryManager, cardViewOrderManager, cardTaiKhoan_Manager,
    cardVoucherManager, cardThongBao, cartThongKeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load user hiện tại
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        allows();
        getToken();
        initView();
        initControll();
    }

    private void allows() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.POST_NOTIFICATIONS
                    }, 101);
                }
            }
    }

    protected void initControll() {
        cardViewProductManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductManagerActivity.class);
                startActivity(intent);
            }
        });
        cardViewCategoryManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CateroryManagerActivity.class);
                startActivity(intent);
            }
        });
        cardViewOrderManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderManagerAcitvity.class);
                startActivity(intent);
            }});

        cartThongKeManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThongKeActivity.class);
                startActivity(intent);
            }
        });

        cardVoucherManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VoucherManagerActivity.class);
                startActivity(intent);
            }
        });
        cardTaiKhoan_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountManagerActivity.class);
                startActivity(intent);
            }
        });
        cardThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_notify, null);
        builder.setView(dialogView);
        builder.setTitle("Thiết lập thông báo");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputEditText edtTitle = dialogView.findViewById(R.id.edtTenTB);
                TextInputEditText edtContent = dialogView.findViewById(R.id.edtNoiDungTB);
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();

                notifyToUser(title, content);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void notifyToUser(String title, String content) {
        compositeDisposable.add(apiBanHang.getRole(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                for (int i=0; i<userModel.getResult().size(); i++){
                                    Map<String, String> data = new HashMap<>();
                                    data.put("title", title);
                                    data.put("body", content);
                                    NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(), data);
                                    ApiPushNotification apiPushNotification = RetrofitClientNoti.getInstance().create(ApiPushNotification.class);
                                    compositeDisposable.add(apiPushNotification.sendNotification(notiSendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notiResponse -> {},
                                                    throwable -> {
                                                        Log.d("Logg", throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getAccount_id(), s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {
                                            },
                                            throwable -> {
                                            }
                                    ));
                        }
                    }
                });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        cardViewProductManager = findViewById(R.id.cardSanPham_Manager);
        cardViewCategoryManager = findViewById(R.id.cardDanhMuc_Manager);
        cartThongKeManager = findViewById(R.id.cardThongKe_Manager);
        cardVoucherManager = findViewById(R.id.cardVoucher_Manager);
        cardThongBao = findViewById(R.id.cardThongBao_Manager);
        toolbar = findViewById(R.id.toolbar_main);
        cardTaiKhoan_Manager = findViewById(R.id.cardTaiKhoan_Manager);
        setSupportActionBar(toolbar);
        cardViewOrderManager = findViewById(R.id.cardDonHang_Manager);
        cardTaiKhoan_Manager = findViewById(R.id.cardTaiKhoan_Manager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuDoiMatKhau) {
            Intent intent = new Intent(getApplicationContext(), ForgotPassActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuDangXuat) {
            Paper.book().delete("user");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}