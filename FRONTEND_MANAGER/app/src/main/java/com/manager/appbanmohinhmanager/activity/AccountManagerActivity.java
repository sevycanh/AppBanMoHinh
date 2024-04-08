package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.AccountManagerAdapter;
import com.manager.appbanmohinhmanager.adapter.VoucherManagerAdapter;
import com.manager.appbanmohinhmanager.model.Account;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountManagerActivity extends AppCompatActivity {
    private Toolbar tb_accountManager;
    private TextView tv_activeAccount, tv_deactiveAccount;
    private View viewDeactive, viewActive;
    private RecyclerView rcv_account;
    AccountManagerAdapter accountManagerAdapter;
    ApiManager apiManager;
    List<Account> accountList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int status = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        AnhXa();
        setActionBar();
        initControl();
        getAccount(status);
    }

    private void setActionBar() {
        setSupportActionBar(tb_accountManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_accountManager.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AnhXa() {
        tb_accountManager = findViewById(R.id.tb_accountManager);
        tv_activeAccount = findViewById(R.id.tv_activeAccount);
        tv_deactiveAccount = findViewById(R.id.tv_deactiveAccount);
        viewDeactive = findViewById(R.id.viewDeactive);
        viewActive = findViewById(R.id.viewActive);
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
        rcv_account = findViewById(R.id.rcv_account);
        rcv_account.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void initControl() {
        tv_activeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_activeAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                tv_deactiveAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                viewActive.setVisibility(View.VISIBLE);
                viewDeactive.setVisibility(View.GONE);
                status = 1;
                getAccount(status);
                accountManagerAdapter.notifyDataSetChanged();

            }
        });

        tv_deactiveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_deactiveAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                tv_activeAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                viewActive.setVisibility(View.GONE);
                viewDeactive.setVisibility(View.VISIBLE);
                status = 0;
                getAccount(status);
                accountManagerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getAccount(int status) {
        compositeDisposable.add(apiManager.getAccountManager(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accountModel -> {
                            if (accountModel.isSuccess()) {
                                accountList = accountModel.getResult();
                                accountManagerAdapter = new AccountManagerAdapter(AccountManagerActivity.this, accountList);
                                rcv_account.setAdapter(accountManagerAdapter);
                                rcv_account.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), accountModel.getMessage(), Toast.LENGTH_LONG).show();
                                rcv_account.setVisibility(View.GONE);
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccount(status);
    }
}