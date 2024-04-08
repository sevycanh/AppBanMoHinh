package com.example.shopmohinh.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopmohinh.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.model.User;
import com.example.shopmohinh.utils.Utils;
import com.example.shopmohinh.adapter.SPMoiAdapter;
import com.example.shopmohinh.fragment.AccountFragment;
import com.example.shopmohinh.fragment.ContactFragment;
import com.example.shopmohinh.fragment.HomeFragment;
import com.example.shopmohinh.fragment.OrderFragment;

import com.example.shopmohinh.adapter.Loaisp_Adapter;
import com.example.shopmohinh.model.LoaiSP;
import com.example.shopmohinh.retrofit.ApiBanHang;

import com.example.shopmohinh.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolBar;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    SearchView searchView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<LoaiSP> mangLoaiSp;
    Loaisp_Adapter loaispAdapter;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    Boolean checkViewSearch = true;

    NotificationBadge badge_main;
    ImageView imgCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load user hiện tại
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }

        allows(); // cap quyen
        Anhxa();
        ActionBar();
        setSearchView();
        handleSearchClicked();
        getLoaiSanPham();
        loadBottomNavView();
        getToken();
        checkIn();
        initControl();
        if(Utils.carts!=null){
            badge_main.setText(String.valueOf(Utils.carts.size()));
        }

        if (isConnected(this)) {
            getLoaiSanPham();
        } else {
            Toast.makeText(getApplicationContext(), "Không có kết nối Internet!", Toast.LENGTH_LONG).show();
        }
        loadFragment(new HomeFragment(), true);
    }

    private void allows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.POST_NOTIFICATIONS
                }, 101);
            }
        }
    }

    private void loadBottomNavView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome) {
                    loadFragment(new HomeFragment(), false);
                    controlToolbar(1);
                } else if (itemId == R.id.navOrder) {
                    clrFrameLayout();
                    loadFragment(new OrderFragment(), false);
                    controlToolbar(2);
                } else if (itemId == R.id.navContact) {
                    clrFrameLayout();
                    controlToolbar(3);
                    loadFragment(new ContactFragment(), false);
                } else { // nav Account
                    clrFrameLayout();
                    controlToolbar(4);
                    loadFragment(new AccountFragment(), false);
                }
                return true;
            }

        });
        }

    private void initControl() {
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCart();
            }
        });
    }

    private void viewCart() {
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }

    private void handleSearchClicked(){
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void checkIn() {
        //check-in để update điểm danh và lượt chơi lucky box
        compositeDisposable.add(apiBanHang.checkIn(Utils.user_current.getAccount_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {},
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getAccount_id(), s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {},
                                            throwable -> {}
                                    ));
                        }
                    }
                });
    }

    private void setSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Tìm kiếm");
    }

    private void ActionBar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        imgCart = findViewById(R.id.imgCart_main);
        toolBar = findViewById(R.id.toolBarHomePage);
        navigationView = findViewById(R.id.navigationHomePage);
        listView = findViewById(R.id.listViewHomePage);
        drawerLayout = findViewById(R.id.drawerLayoutHomePage);
        searchView = findViewById(R.id.searchHomePage);
        // retrofit get data tbl_category
        mangLoaiSp = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        badge_main = findViewById(R.id.menu_quantity_main);
        if(Utils.carts!=null){
            badge_main.setText(String.valueOf(Utils.carts.size()));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        badge_main.setText(String.valueOf(Utils.carts.size()));
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSPModel -> {
                            if (loaiSPModel.isSuccess()) {
                                mangLoaiSp = loaiSPModel.getResult();
                                loaispAdapter = new Loaisp_Adapter(getApplicationContext(), mangLoaiSp);
                                listView.setAdapter(loaispAdapter);
                            }
                        }, throwable -> {
                            Toast.makeText(this,throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    private void clrFrameLayout() {
        frameLayout = findViewById(R.id.frameLayout);
        frameLayout.removeAllViews();
    }

    private void controlToolbar(int id) {
        if (id != 1) {
            toolBar.setVisibility(View.GONE);
        } else {
            toolBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}