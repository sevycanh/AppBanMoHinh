package com.example.shopmohinh.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogInActivity extends AppCompatActivity {
    EditText edtEmail, edtPass;
    Button btnSignIn;
    TextView txtForgot, txtSignUp, txtSaiThongTin;
    CircleImageView circleImageView;
    FirebaseAuth firebaseAuth;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    GoogleSignInClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        FirebaseApp.initializeApp(this);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                        .requestEmail()
                                .build();
        client = GoogleSignIn.getClient(this, options);
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
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                            dangNhap(email);
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                                            builder.setTitle("Thông báo");
                                            builder.setMessage("Vui lòng kiểm tra email để thực hiện xác thực trước khi đăng nhập!");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    } else {
                                        txtSaiThongTin.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = client.getSignInIntent();
                activityResultLauncher.launch(i);
            }
        });
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

                if (o.getResultCode() == RESULT_OK){
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                        firebaseAuth.signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
//                                            dangNhap(firebaseAuth.getCurrentUser().getEmail());
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } catch (ApiException e){
                        e.printStackTrace();
                    }
                }
            }

    });

    private void initView() {
        Paper.init(this);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        txtSignUp = findViewById(R.id.txtSignUp_Login);
        txtForgot = findViewById(R.id.txtForgot);
        edtEmail = findViewById(R.id.edtEmail_signIn);
        edtPass = findViewById(R.id.edtPassword_signIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSaiThongTin = findViewById(R.id.txt_SaiThongTin);
        circleImageView = findViewById(R.id.signIn_Google);

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