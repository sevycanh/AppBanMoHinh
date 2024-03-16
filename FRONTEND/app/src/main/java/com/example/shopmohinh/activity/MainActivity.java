package com.assignments.toystore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.assignments.toystore.R;
import com.assignments.toystore.adapter.CategoryAdapter;
import com.assignments.toystore.model.Category;
import com.assignments.toystore.retrofit.RetrofitClient;
import com.assignments.toystore.retrofit.SalesApi;
import com.assignments.toystore.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewScreenMain;
    ListView listViewScreenMain;
    NavigationView navigationView;
    CategoryAdapter categoryAdapter;
    List<Category> categories;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SalesApi salesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        salesApi = RetrofitClient.getInstance(Utils.BASE_URL).create(SalesApi.class);
        Mapping();
        ActionBar();

        if (isConnected(this)) {
            ActionViewFlipper();
            getCategory();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "không có internet , vui lòng kết nối !!", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewScreenMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        break;
                    case 1:
                        Intent luffy = new Intent(getApplicationContext(), ProductActivity.class);
                        luffy.putExtra("id_category",1);
                        startActivity(luffy);
                        break;
                    case 2:
                        Intent goku = new Intent(getApplicationContext(), ProductActivity.class);
                        goku.putExtra("id_category",2);
                        startActivity(goku);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
            }
        });
    }

    private void getCategory() {
        compositeDisposable.add(salesApi.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if (categoryModel.isSuccess()) {
                                categories = categoryModel.getResult();
                                categoryAdapter = new CategoryAdapter(getApplicationContext(), categories);
                                listViewScreenMain.setAdapter(categoryAdapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "abc: false", Toast.LENGTH_LONG).show();
                            }
                        }
                )
        );
    }

    private void ActionViewFlipper() {
        List<String> lists = new ArrayList<>();
        lists.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        lists.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        lists.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i = 0; i < lists.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(lists.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Mapping() {
        toolbar = findViewById(R.id.toolbar_screen_main);
        viewFlipper = findViewById(R.id.id_view_flipper);
        recyclerViewScreenMain = findViewById(R.id.recycleview);
        listViewScreenMain = findViewById(R.id.listview_screen_main);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.draw_layout);

        //create List
        categories = new ArrayList<>();
        //create Adapter
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categories);
        listViewScreenMain.setAdapter(categoryAdapter);
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo moblie = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (moblie != null && moblie.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}