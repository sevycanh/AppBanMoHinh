package com.example.shopmohinh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.example.shopmohinh.R;
import com.example.shopmohinh.Utils.Utils;
import com.example.shopmohinh.adapter.SPMoiAdapter;
import com.example.shopmohinh.fragment.AccountFragment;
import com.example.shopmohinh.fragment.ContactFragment;
import com.example.shopmohinh.fragment.HomeFragment;
import com.example.shopmohinh.fragment.OrderFragment;
import com.example.shopmohinh.model.SanPhamMoi;

import com.example.shopmohinh.adapter.Loaisp_Adapter;
import com.example.shopmohinh.model.LoaiSP;
import com.example.shopmohinh.retrofit.ApiBanHang;

import com.example.shopmohinh.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolBar;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    ImageSlider imageSlider;
    SearchView searchView;
    SPMoiAdapter spMoiAdapter;
    List<SanPhamMoi> mangSanPhamMoi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<LoaiSP> mangLoaiSp;
    Loaisp_Adapter loaispAdapter;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    Fragment orderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        apiSanPhamMoi = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPhamMoi.class);
        Anhxa();
        ActionBar();
        setSearchView();
        if (isConnected(this)) {
//            Toast.makeText(getApplicationContext(), "OK!", Toast.LENGTH_LONG).show();
//            ActionViewFlipper();
            getSanPhamMoi();
            getLoaiSanPham();
        } else {
            Toast.makeText(getApplicationContext(), "Không có kết nối Internet!", Toast.LENGTH_LONG).show();
        }
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
        loadFragment(new HomeFragment(), true);
    }

    private void getSanPhamMoi() {


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

//    private void ActionViewFlipper() {
//
//        List<SlideModel> ArrayQuangCao = new ArrayList<>();
//        ArrayQuangCao.add(new SlideModel("https://treobangron.com.vn/wp-content/uploads/2022/09/banner-khuyen-mai-42.jpg", null));
//        ArrayQuangCao.add(new SlideModel("https://treobangron.com.vn/wp-content/uploads/2022/09/banner-khuyen-mai-40.jpg", null));
//        ArrayQuangCao.add(new SlideModel("https://treobangron.com.vn/wp-content/uploads/2022/09/banner-khuyen-mai-23.jpg", null));
//        imageSlider.setImageList(ArrayQuangCao, ScaleTypes.CENTER_CROP);
//
//        imageSlider.setOnTouchListener(touchListener);
//    }

//    View.OnTouchListener touchListener = new View.OnTouchListener() {
//        private float startX;
//        private float startY;
//        private long startTime;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    startX = event.getX();
//                    startY = event.getY();
//                    startTime = System.currentTimeMillis();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    float endX = event.getX();
//                    float endY = event.getY();
//                    long endTime = System.currentTimeMillis();
//
//                    float deltaX = endX - startX;
//                    float deltaY = endY - startY;
//                    long deltaTime = endTime - startTime;
//
//                    // Tính toán khoảng cách và thời gian để xác định hướng di chuyển
//                    if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 100 && deltaTime < 200) {
//                        // Xác định người dùng đã kéo sang trái hay sang phải
//                        if (deltaX > 0) {
//                            // Kéo sang phải
//                            imageSlider.startSliding(10000);
//                        } else {
//                            // Kéo sang trái
//                            imageSlider.startSliding(10000);
//                        }
//                    }
//                    break;
//            }
//            return true;
//        }
//
//
//    };

    private void Anhxa() {
        toolBar = findViewById(R.id.toolBarHomePage);
//        recyclerView = findViewById(R.id.recyclerViewHomePage);
        navigationView = findViewById(R.id.navigationHomePage);
        listView = findViewById(R.id.listViewHomePage);
        drawerLayout = findViewById(R.id.drawerLayoutHomePage);
//        imageSlider = findViewById(R.id.imageSliderHomePage);
        searchView = findViewById(R.id.searchHomePage);
        //Khoi tao list
//        mangSanPhamMoi = new ArrayList<>();

        //Khoi tao Adapter
//        spMoiAdapter = new SPMoiAdapter(getApplicationContext(), mangSanPhamMoi);
//        listView.setAdapter(spMoiAdapter);

        // retrofit get data tbl_category
        mangLoaiSp = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

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
//                                Toast.makeText(getApplicationContext(),loaiSPModel.getResult().get(0).getName(), Toast.LENGTH_LONG).show();
                                mangLoaiSp = loaiSPModel.getResult();
                                loaispAdapter = new Loaisp_Adapter(getApplicationContext(), mangLoaiSp);
                                ListView listView = findViewById(R.id.listViewHomePage);
                                listView.setAdapter(loaispAdapter);
                            }
                        }
                ));
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
//            Toast.makeText(this, "Fragment", Toast.LENGTH_SHORT).show();

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

}