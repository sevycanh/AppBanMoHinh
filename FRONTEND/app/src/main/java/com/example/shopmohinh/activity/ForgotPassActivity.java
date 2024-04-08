package com.example.shopmohinh.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {
    TextInputEditText edtEmail;
    Button btnXacNhan, btnQuayLai;
    CircularProgressIndicator progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        initView();
        initControll();
    }

    private void initControll() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập chính xác email", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(
                                    task -> {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Đã gửi", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                }
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        edtEmail = findViewById(R.id.edtEmail_Forgot);
        btnXacNhan = findViewById(R.id.btnXacNhan_Forgot);
        btnQuayLai = findViewById(R.id.btnCancel_Forgot);
        progressBar = findViewById(R.id.progressbar_Forgot);
    }
}