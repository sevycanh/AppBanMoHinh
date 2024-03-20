package com.example.shopmohinh.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shopmohinh.R;
import com.example.shopmohinh.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

public class AddressUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText edtUsername_Order,edtPhone_Order,edtAddress_Order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_user);
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
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarAddressUser);
        edtUsername_Order = findViewById(R.id.edtUsername_Order);
        edtPhone_Order = findViewById(R.id.edtPhone_Order);
        edtAddress_Order = findViewById(R.id.edtAddress_Order);
    }


}