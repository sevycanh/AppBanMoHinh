package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.model.User;
import com.manager.appbanmohinhmanager.retrofit.ApiBanHang;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    CardView cardViewProductManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load user hiện tại
        Paper.init(this);
        if (Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        getToken();
        initView();
        initControll();
    }

    protected void initControll(){
        cardViewProductManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getAccount_id(), s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {},
                                            throwable -> {}
                                    ));
                        }
                    }
                });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        cardViewProductManager = findViewById(R.id.cardSanPham_Manager);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menuDoiMatKhau){
            Toast.makeText(this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId()==R.id.menuDangXuat){
            Paper.book().delete("user");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}