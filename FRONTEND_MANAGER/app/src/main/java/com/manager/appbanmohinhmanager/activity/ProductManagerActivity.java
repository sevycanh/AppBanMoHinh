package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.ProductManagerAdapter;
import com.manager.appbanmohinhmanager.model.CategoryManager;
import com.manager.appbanmohinhmanager.model.ProductManager;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductManagerActivity extends AppCompatActivity {
    ImageView btnAdd;
    RecyclerView recyclerView;
    List<ProductManager> mangSP;
    LinearLayoutManager linearLayoutManager;
    ApiManager apiManager;
    ProductManagerAdapter productManagerAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MaterialToolbar toolbar;

    List<CategoryManager> arrayCategory;
    Spinner spinner;
    int idcategory = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        initView();
        getDataProductManager();
        handleClickedButton();
        actionToolBar();
        getCategory();
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

    private void handleClickedButton() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                Bundle bundle = new Bundle();
                int nextid;
                if (mangSP.size() == 0){
                    nextid = 0;
                }else {
                    nextid = mangSP.get(mangSP.size()-1).getProduct_id();
                }
                bundle.putInt("nextid", nextid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        btnAdd = findViewById(R.id.btnThem);
        recyclerView = findViewById(R.id.listProduct);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mangSP = new ArrayList<>();
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
        toolbar = findViewById(R.id.toolBarPM);
        spinner = findViewById(R.id.Category_Product_Manager);
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
                            Toast.makeText(getApplicationContext(), "404", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void getDataProductManagerFindByCategory(int categoryid) {
        compositeDisposable.add(apiManager.getDataProductFindByCategory(categoryid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productManagerModel -> {
                            mangSP = productManagerModel.getResult();
                            productManagerAdapter = new ProductManagerAdapter(getApplicationContext(), mangSP);
                            recyclerView.setAdapter(productManagerAdapter);
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), "404", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }


    private void getCategory () {
        compositeDisposable.add(apiManager.getDataCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        CategoryManagerModel -> {
                            if (CategoryManagerModel.isSuccess()) {
                                arrayCategory = CategoryManagerModel.getResult();
                                List<String> data = new ArrayList<>();
                                data.add("Tất Cả");
                                for (int i = 0; i < arrayCategory.size(); i++) {
                                    int id = arrayCategory.get(i).getCategory_id();
                                    String s = arrayCategory.get(i).getName();
                                    data.add(s);
                                }
                                ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
                                spinner.setAdapter(adapterCategory);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position == 0){
                                            getDataProductManager();
                                        }else {
                                            Toast.makeText(ProductManagerActivity.this, String.valueOf(arrayCategory.get(position-1).getCategory_id()), Toast.LENGTH_SHORT).show();
                                            idcategory = arrayCategory.get(position-1).getCategory_id();
                                            getDataProductManagerFindByCategory(idcategory);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    @Override
    protected void onResume() {
        if (idcategory == -1){
            getDataProductManager();
        }else {
            getDataProductManagerFindByCategory(idcategory);
        }
        super.onResume();
    }
}