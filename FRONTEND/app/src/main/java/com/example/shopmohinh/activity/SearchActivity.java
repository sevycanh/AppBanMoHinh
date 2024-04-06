package com.example.shopmohinh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.adapter.SpSearchAdapter;
import com.example.shopmohinh.model.SanPhamSearch;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    SearchView searchView;
    SpSearchAdapter searchAdapter;
    List<SanPhamSearch> mangSpSearch;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    GridLayoutManager gridLayoutManager;
    String typeGlobal = "";
    String nameGlobal = "";
    String sortGlobal = "";
    Button btnLienQuan, btnMoiNhat, btnKhuyenMai, btnGia;
    View viewLienQuan, viewMoiNhat, viewKhuyenMai, viewGia;
    Drawable icon_down, icon_up, icon_default;
    NotificationBadge badge_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AnhXa();
        linearLayout.setVisibility(View.GONE);
        actionToolBar();
        searchView.requestFocus();
        handleSearch();
    }
    private void clearButtonView(){
        btnGia.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_default, null);
        viewLienQuan.setVisibility(View.GONE);
        btnLienQuan.setTextColor(Color.BLACK);
        viewMoiNhat.setVisibility(View.GONE);
        btnMoiNhat.setTextColor(Color.BLACK);
        viewKhuyenMai.setVisibility(View.GONE);
        btnKhuyenMai.setTextColor(Color.BLACK);
        viewGia.setVisibility(View.GONE);
        btnGia.setTextColor(Color.BLACK);
    }

    private void handleSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clearButtonView();
                typeGlobal = "lienquan";
                nameGlobal = query;
                linearLayout.setVisibility(View.VISIBLE);
                viewLienQuan.setVisibility(View.VISIBLE);
                btnLienQuan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                getSanPhamSearch(typeGlobal, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        btnLienQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButtonView();
                typeGlobal = "lienquan";
                viewLienQuan.setVisibility(View.VISIBLE);
                btnLienQuan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                getSanPhamSearch(typeGlobal, nameGlobal);
            }
        });
        btnMoiNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButtonView();
                typeGlobal = "moinhat";
                viewMoiNhat.setVisibility(View.VISIBLE);
                btnMoiNhat.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                getSanPhamSearch(typeGlobal, nameGlobal);
            }
        });
        btnKhuyenMai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButtonView();
                typeGlobal = "khuyenmai";
                viewKhuyenMai.setVisibility(View.VISIBLE);
                btnKhuyenMai.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                getSanPhamSearch(typeGlobal, nameGlobal);
            }
        });
        btnGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortGlobal == "" || sortGlobal == "ASC"){
                    sortGlobal = "DESC";
                    clearButtonView();
                    viewGia.setVisibility(View.VISIBLE);
                    btnGia.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                    btnGia.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_down, null);
                    getSanPhamSearch(sortGlobal, nameGlobal);
                }
                else {
                    sortGlobal = "ASC";
                    clearButtonView();
                    viewGia.setVisibility(View.VISIBLE);
                    btnGia.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                    btnGia.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_up, null);
                    getSanPhamSearch(sortGlobal, nameGlobal);
                }
            }
        });
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AnhXa(){
        icon_default = getResources().getDrawable(R.drawable.default_arrow);
        icon_down = getResources().getDrawable(R.drawable.arrow_down);
        icon_up = getResources().getDrawable(R.drawable.arrow_up);
        linearLayout = findViewById(R.id.linearInSearchView);
        btnLienQuan = findViewById(R.id.btnLienQuan);
        btnMoiNhat = findViewById(R.id.btnMoiNhat);
        btnKhuyenMai = findViewById(R.id.btnKhuyenMai);
        btnGia = findViewById(R.id.btnGia);
        viewLienQuan = findViewById(R.id.viewLienQuan);
        viewMoiNhat = findViewById(R.id.viewMoiNhat);
        viewKhuyenMai = findViewById(R.id.viewKhuyenMai);
        viewGia = findViewById(R.id.viewGia);
        searchView = findViewById(R.id.searchView);
        toolbar = findViewById(R.id.toolBarSearch);
        recyclerView = findViewById(R.id.recyclerViewSearch);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mangSpSearch = new ArrayList<>();
        badge_search = findViewById(R.id.menu_quantity_search);
        if(Utils.carts!=null){
            badge_search.setText(String.valueOf(Utils.carts.size()));
        }
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        badge_search.setText(String.valueOf(Utils.carts.size()));
    }

    private void getSanPhamSearch(String type, String tensp){
        compositeDisposable.add(apiBanHang.searchSp(type, tensp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamSearchModel -> {
                            mangSpSearch = sanPhamSearchModel.getResult();
                            searchAdapter = new SpSearchAdapter(getApplicationContext(), mangSpSearch);
                            recyclerView.setAdapter(searchAdapter);
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(),"Lỗi tìm kiếm", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }
}