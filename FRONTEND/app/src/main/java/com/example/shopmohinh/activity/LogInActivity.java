package com.example.shopmohinh.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.Utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogInActivity extends AppCompatActivity {
    EditText edtEmail, edtPass;
    Button btnSignIn;
    TextView txtForgot, txtSignUp, txtSaiThongTin;

    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initView();
        initControl();
    }

    private void initControl() {
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
//                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập password", Toast.LENGTH_SHORT).show();
                } else {
                    //save acc
                    Paper.book().write("email", email);
                    Paper.book().write("pass", pass);

                    dangNhap(email, pass);
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        txtSignUp = findViewById(R.id.txtSignUp_Login);
        txtForgot = findViewById(R.id.txtForgot);
        edtEmail = findViewById(R.id.edtEmail_signIn);
        edtPass = findViewById(R.id.edtPassword_signIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSaiThongTin = findViewById(R.id.txt_SaiThongTin);

        if (Paper.book().read("email") != null && Paper.book().read("pass") != null){
            edtEmail.setText(Paper.book().read("email"));
            edtPass.setText(Paper.book().read("pass"));
            if (Paper.book().read("isLogin") != null){
                boolean flag = Paper.book().read("isLogin");
                if (flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            dangNhap(Paper.book().read("email"), Paper.book().read("pass"));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void dangNhap(String email, String pass) {
        compositeDisposable.add(apiBanHang.dangNhap(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);

                                Utils.user_current = userModel.getResult().get(0);

                                //Luu lai thong tin nguoi dung
                                Paper.book().write("user", userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                txtSaiThongTin.setVisibility(View.VISIBLE);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPassword() != null){
            edtEmail.setText(Utils.user_current.getEmail());
            edtPass.setText(Utils.user_current.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}