package com.example.shopmohinh.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopmohinh.R;
import com.example.shopmohinh.utils.Utils;

public class ChangePayment extends AppCompatActivity {
    RadioButton radioButtonZalo, radioButtonCash;

    Button saveButtonPayment;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_payment);

        initView();
        initControl();
    }



    private void initView() {
        toolbar = findViewById(R.id.toolBarChangePayment);
        radioButtonCash = findViewById(R.id.radioButtonCash);
        radioButtonZalo = findViewById(R.id.radioButtonZalo);
        saveButtonPayment = findViewById(R.id.saveButtonPayment);
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
        if(Utils.method_payment.equals("COD")){
            radioButtonCash.setChecked(true);
        }
        else if(Utils.method_payment.equals("Zalo")){
            radioButtonZalo.setChecked(true);
        }

        saveButtonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButtonCash.isChecked()) {
                    Utils.method_payment = "COD";
                } else if (radioButtonZalo.isChecked()) {
                    Utils.method_payment = "Zalo";
                }
                finish();
            }
        });
    }


}