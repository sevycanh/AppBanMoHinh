package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.ProductManagerAdapter;
import com.manager.appbanmohinhmanager.model.ProductManager;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ProductManagerActivity extends AppCompatActivity {
    ImageView btnAdd;
    Button btnUpdate;
    RecyclerView recyclerView;
    List<ProductManager> mangSP;
    LinearLayoutManager linearLayoutManager;
    ApiManager apiManager;
    ProductManagerAdapter productManagerAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        initView();
        handleClickedButton();
        getDataProductManager();
    }

    private void handleClickedButton() {
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

    private void initView() {
        btnAdd = findViewById(R.id.btnThem);
        btnUpdate = findViewById(R.id.btnSua);
        recyclerView = findViewById(R.id.listProduct);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mangSP = new ArrayList<>();
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);

    }

    private void getDataProductManager() {
        compositeDisposable.add(apiManager.getDataProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productManagerModel -> {
                            mangSP = productManagerModel.getResult();
                            productManagerAdapter = new ProductManagerAdapter(getApplicationContext(), mangSP);
                            recyclerView.setAdapter(productManagerAdapter);
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), "Lỗi tìm kiếm", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }
}