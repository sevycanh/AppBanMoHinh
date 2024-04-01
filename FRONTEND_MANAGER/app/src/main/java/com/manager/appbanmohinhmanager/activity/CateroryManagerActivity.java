package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.CategoryManagerAdapter;
import com.manager.appbanmohinhmanager.model.CategoryManager;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CateroryManagerActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<CategoryManager> mangDanhMuc;
    LinearLayoutManager linearLayoutManager;
    ApiManager apiManager;
    CategoryManagerAdapter categoryManagerAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caterory_manager);
        initView();
        actionToolBar();
        getDataCategoryManager();
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView(){

        toolbar = findViewById(R.id.toolbarDanhMuc);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.listCategory);
        recyclerView.setLayoutManager(linearLayoutManager);
        mangDanhMuc = new ArrayList<>();
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);

    }

    private void getDataCategoryManager(){
        compositeDisposable.add(apiManager.getDataCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryManagerModel -> {
                            mangDanhMuc = categoryManagerModel.getResult();
                            categoryManagerAdapter = new CategoryManagerAdapter(getApplicationContext(), mangDanhMuc);
                            recyclerView.setAdapter(categoryManagerAdapter);
                        },throwable -> {
                            Toast.makeText(getApplicationContext(), "Lỗi tìm kiếm", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

}


