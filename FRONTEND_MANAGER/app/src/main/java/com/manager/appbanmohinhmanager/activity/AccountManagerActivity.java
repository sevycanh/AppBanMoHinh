package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.manager.appbanmohinhmanager.R;

public class AccountManagerActivity extends AppCompatActivity {
    Toolbar tb_accountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        AnhXa();
        setAcionBar();
    }
    private void setAcionBar(){
        setSupportActionBar(tb_accountManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_accountManager.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void AnhXa(){
        tb_accountManager = findViewById(R.id.tb_accountManager);
    }
}