package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.manager.appbanmohinhmanager.R;

public class ProductManagerActivity extends AppCompatActivity {
    Button btnAdd;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        initView();
        handleClickedButton();
    }

    private void handleClickedButton(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView(){
        btnAdd = findViewById(R.id.btnThem);
        btnUpdate = findViewById(R.id.btnSua);
    }
}