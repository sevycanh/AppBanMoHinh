package com.manager.appbanmohinhmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.retrofit.ApiBanHang;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogInActivity extends AppCompatActivity {
    TextInputEditText edtEmail, edtPass;
    Button btnSignIn;
    TextView txtForgot, txtSaiThongTin;
    FirebaseAuth firebaseAuth;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initView();
        initControl();
    }

    private void initControl() {

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassActivity.class);
                startActivity(intent);
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
                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        dangNhap(email);
                                    } else {
                                        txtSaiThongTin.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        txtForgot = findViewById(R.id.txtForgot);
        edtEmail = findViewById(R.id.edtEmail_signIn);
        edtPass = findViewById(R.id.edtPassword_signIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSaiThongTin = findViewById(R.id.txt_SaiThongTin);

        firebaseAuth = FirebaseAuth.getInstance();

        if (Paper.book().read("email") != null){
            edtEmail.setText(Paper.book().read("email"));
        }
    }

    private void dangNhap(String email) {
        compositeDisposable.add(apiBanHang.dangNhap(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                if (userModel.getResult().get(0).getRole() == 0){
                                    Utils.user_current = userModel.getResult().get(0);
                                    //Lưu lại thông tin người dùng
                                    Paper.book().write("user", userModel.getResult().get(0));
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //Không có quyền admin
                                    txtSaiThongTin.setVisibility(View.VISIBLE);
                                }
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
        if (Utils.user_current.getEmail() != null){
            edtEmail.setText(Utils.user_current.getEmail());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}