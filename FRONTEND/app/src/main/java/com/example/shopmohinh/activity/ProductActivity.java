package com.example.shopmohinh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmohinh.R;
import com.example.shopmohinh.adapter.ProductAdapter;
import com.example.shopmohinh.model.Cart;
import com.example.shopmohinh.model.Product;
import com.example.shopmohinh.model.User;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.retrofit.SalesApi;
import com.example.shopmohinh.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
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
    ProductAdapter phoneAdapter;
    List<Product> productList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    ApiBanHang apiBanHang;
    Product productTemp;
    NotificationBadge badge_product;
    ImageView imgCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.shopmohinh.R.layout.activity_product);
        salesApi = RetrofitClient.getInstance(Utils.BASE_URL).create(SalesApi.class);
        category = getIntent().getIntExtra("category", 1);
        Mapping();
        ActionToolBar();
        getData(page);
        addEventLoad();
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        initCart();
        initControl();
    }

    private void initControl() {
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCart();
            }
        });
    }

    private void initCart() {
        compositeDisposable.add(apiBanHang.getShoppingCart(Utils.user_current.getAccount_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                productList = productModel.getResult();
                                productToCart(productList);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void productToCart(List<Product> productList) {
        for (int i = 0; i < productList.size(); i++) {
            productTemp = productList.get(i);
            Cart cartTemp = new Cart();
            cartTemp.setIdProduct(productTemp.getProduct_id());
            cartTemp.setQuantity(productTemp.getQuantity());
            cartTemp.setName(productTemp.getName());
            int finalPrice = productTemp.getPrice() - (productTemp.getPrice() * productTemp.getCoupon() / 100);
            cartTemp.setPrice(finalPrice);
            cartTemp.setImage(productTemp.getMain_image());
            Utils.carts.add(cartTemp);
        }
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
                if (isLoading == false) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == productList.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        badge_product.setText(String.valueOf(Utils.carts.size()));
    }

    private void viewCart() {
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }
    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                //productList.add(null);
                //phoneAdapter.notifyItemInserted(productList.size() - 1);
                Toast.makeText(getApplicationContext(), "Đang tải ...", Toast.LENGTH_LONG).show();
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remove null
                //productList.remove(productList.size() - 1);
                //phoneAdapter.notifyItemRemoved(productList.size());
                page = page + 1;
                getData(page);
                //phoneAdapter.notifyDataSetChanged();
                phoneAdapter.loadNewData(productList);
                isLoading = false;
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(salesApi.getProduct(page, category)
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                if (phoneAdapter == null) {
                                    productList = productModel.getResult();
                                    phoneAdapter = new ProductAdapter(getApplicationContext(), productList);
                                    recyclerView.setAdapter(phoneAdapter);
                                } else {
                                    int position = productList.size() - 1;
                                    int numberAdd = productModel.getResult().size();
                                    for (int i = 0; i < numberAdd; i++) {
                                        productList.add(productModel.getResult().get(i));
                                    }
                                    phoneAdapter.notifyItemRangeInserted(position, numberAdd);
                                }
                            } else {
                                isLoading = true;
                                Toast.makeText(getApplicationContext(), "Hết sản phẩm", Toast.LENGTH_LONG).show();
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
        imgCart = findViewById(R.id.imgCart_SP);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_phone);
        linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        productList = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        badge_product = findViewById(R.id.menu_quantity_product);
        if (Utils.carts != null) {
            badge_product.setText(String.valueOf(Utils.carts.size()));
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}