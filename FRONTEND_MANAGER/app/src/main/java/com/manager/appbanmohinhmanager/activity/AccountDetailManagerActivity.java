package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountDetailManagerActivity extends AppCompatActivity {
    Toolbar tb_accountDetailManager;
    int id, status, coin;
    String username, date, phone, email;
    TextView tv_id, tv_status, tv_coin, tv_username, tv_date, tv_phone, tv_email;
    Button btn_ChangeStatus;
    ApiManager apiManager;
    CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_manager);
        AnhXa();
        setActionBarDetailAccount();
        setDataAccount();
        btn_ChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 1) {
                    status = 0;
                } else {
                    status = 1;
                }
                Toast.makeText(AccountDetailManagerActivity.this, status + " " + id, Toast.LENGTH_SHORT).show();
                cancelOrder(status, id);
            }
        });
    }

    private void AnhXa() {
        tb_accountDetailManager = findViewById(R.id.tb_accountDetailManager);
        tv_id = findViewById(R.id.tv_id_accountDetail);
        tv_status = findViewById(R.id.tv_status_accountDetail);
        tv_coin = findViewById(R.id.tv_coin_accountDetail);
        tv_username = findViewById(R.id.tv_username_accountDetail);
        tv_date = findViewById(R.id.tv_date_accountDetail);
        tv_phone = findViewById(R.id.tv_phone_accountDetail);
        tv_email = findViewById(R.id.tv_email_accountDetail);
        btn_ChangeStatus = findViewById(R.id.btn_ChangeStatus_AccountDetail);
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
        compositeDisposable = new CompositeDisposable();
    }

    private void setActionBarDetailAccount() {
        setSupportActionBar(tb_accountDetailManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_accountDetailManager.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setDataAccount() {
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id"));
        status = Integer.parseInt(intent.getStringExtra("status"));
        coin = Integer.parseInt(intent.getStringExtra("coin"));
        email = intent.getStringExtra("email");
        username = intent.getStringExtra("username");
        phone = intent.getStringExtra("phone");
        date = intent.getStringExtra("date");
        //set tv
        tv_id.setText(String.valueOf(id));
        if (status == 1) {
            tv_status.setText("Hoạt động");
            tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_Active_account));
        } else {
            tv_status.setText("Đã khóa");
            tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_Deactive_account));
        }
        tv_coin.setText(String.valueOf(coin));
        if (username.equals("")) {
            tv_username.setText("Chưa cập nhật");
        } else {
            tv_username.setText(username);
        }
        if (phone.equals("")) {
            tv_phone.setText("Chưa cập nhật");
        } else {
            tv_phone.setText(phone);
        }
        tv_date.setText(date);
        tv_email.setText(email);
        if (status == 0) {
            btn_ChangeStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green_Active_account));
            btn_ChangeStatus.setText("Mở tài khoản");
        } else {
            btn_ChangeStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red_Deactive_account));
            btn_ChangeStatus.setText("Khóa tài khoản");
        }
    }

    private void changeStatus(int status, int accountid) {
        compositeDisposable.add(apiManager.updateAccountStatus(status, accountid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accountModel -> {
                            if (accountModel.isSuccess()) {
                                Toast.makeText(this, "update thanh cong", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "update that bai", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Log.d("Loi update Account Status", throwable.getMessage());
                        }
                ));
    }

    private void cancelOrder(int status, int accountid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailManagerActivity.this);
        builder.setTitle("Thay đổi trạng thái tài khoản");
        builder.setMessage("Bạn có chắc muốn thay đổi không ?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changeStatus(status, accountid);
                if (status == 1) {
                    btn_ChangeStatus.setText("Khóa tài khoản");
                    btn_ChangeStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red_Deactive_account));
                    tv_status.setText("Hoạt động");
                    tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_Active_account));
                } else {
                    btn_ChangeStatus.setText("Mở tài khoản");
                    btn_ChangeStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green_Active_account));
                    tv_status.setText("Đã khóa");
                    tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_Deactive_account));
                }
                Toast.makeText(AccountDetailManagerActivity.this, "Dong y", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}