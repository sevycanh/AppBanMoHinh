package com.example.shopmohinh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogInActivity extends AppCompatActivity {
    EditText edtEmail, edtPass;
    Button btnSignIn;
    TextView txtForgot, txtSignUp, txtSaiThongTin;
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

        txtSignUp = findViewById(R.id.txtSignUp_Login);
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