package com.assignments.toystore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.assignments.toystore.R;
import com.assignments.toystore.adapter.PhoneAdapter;
import com.assignments.toystore.model.Product;
import com.assignments.toystore.retrofit.RetrofitClient;
import com.assignments.toystore.retrofit.SalesApi;
import com.assignments.toystore.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    SalesApi salesApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int category;
    PhoneAdapter phoneAdapter;
    List<Product> productList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    private int currentItemCount = 0;
    private int maxItems = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        salesApi = RetrofitClient.getInstance(Utils.BASE_URL).create(SalesApi.class);
        category = getIntent().getIntExtra("id_category", 1);
        Mapping();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && currentItemCount < maxItems) {
                    int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();

                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                productList.add(null);
                phoneAdapter.notifyItemInserted(productList.size() - 1);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remove null
                productList.remove(productList.size() - 1);
                phoneAdapter.notifyItemRemoved(productList.size());
                page = page + 1;
                getData(page);
                phoneAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 3000);
    }

    private void getData(int page) {
        compositeDisposable.add(salesApi.getProduct(page, category)
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                if (phoneAdapter == null) {
                                    productList = productModel.getResult();
                                    phoneAdapter = new PhoneAdapter(getApplicationContext(), productList);
                                    recyclerView.setAdapter(phoneAdapter);
                                    currentItemCount = productList.size();
                                } else {
                                    int position = productList.size() - 1;
                                    int numberAdd = productModel.getResult().size();
                                    for (int i = 0; i < numberAdd; i++) {
                                        productList.add(productModel.getResult().get(i));
                                    }
                                    phoneAdapter.notifyItemRangeInserted(position, numberAdd);
                                    currentItemCount += numberAdd;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu rồi", Toast.LENGTH_LONG).show();
                                isLoading = true;
                            }
                            if (currentItemCount >= maxItems) {
                                isLoading = true; // Đã hiển thị đủ số lượng sản phẩm, không cần tải thêm
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Mapping() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_phone);
        linearLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        productList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}