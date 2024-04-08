package com.example.shopmohinh.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shopmohinh.R;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddressUserActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextInputEditText edtUsername_Order,edtPhone_Order,edtAddress_Order;
    Button btnSaveAddress;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_user);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        initControl();
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.d("INFO", Utils.user_current.getUsername() + "|" + Utils.user_current.getPhone() + "|" + Utils.user_current.getAddress());
        edtUsername_Order.setText(Utils.user_current.getUsername());
        edtPhone_Order.setText(String.valueOf(Utils.user_current.getPhone()));
        edtAddress_Order.setText(Utils.user_current.getAddress());
        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAddress();
            }
        });
    }

    private void SaveAddress() {
        String name = edtUsername_Order.getText().toString();
        String phone = edtPhone_Order.getText().toString();
        String address = edtAddress_Order.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Utils.user_current.setUsername(name);
        Utils.user_current.setAddress(address);
        Utils.user_current.setPhone(phone);
        Paper.book().write("user", Utils.user_current);

        compositeDisposable.add(apiBanHang.updateProfile(Utils.user_current.getAccount_id(),name, phone,address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
                            }
//                            Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarAddressUser);
        edtUsername_Order = findViewById(R.id.edtUsername_Order);
        edtPhone_Order = findViewById(R.id.edtPhone_Order);
        edtAddress_Order = findViewById(R.id.edtAddress_Order);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);

    }


}