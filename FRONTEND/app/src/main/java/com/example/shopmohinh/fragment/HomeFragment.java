package com.example.shopmohinh.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shopmohinh.R;
import com.example.shopmohinh.Utils.Utils;
import com.example.shopmohinh.adapter.Loaisp_Adapter;
import com.example.shopmohinh.adapter.SPMoiAdapter;
import com.example.shopmohinh.model.LoaiSP;
import com.example.shopmohinh.model.SanPhamMoi;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.ApiSanPhamMoi;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
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
    ApiSanPhamMoi apiSanPhamMoi;
    ApiBanHang apiBanHang;
    List<LoaiSP> mangLoaiSp;
    Loaisp_Adapter loaispAdapter;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    Fragment orderFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        apiSanPhamMoi = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPhamMoi.class);
        Anhxa(rootView);
        ActionViewFlipper();
        setSearchView();
        ActionBar();
        getLoaiSanPham();
        return rootView;
    }

    private void setSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Tìm kiếm");
    }

    private void ActionBar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolBar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);

            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    private void ActionViewFlipper() {
        List<SlideModel> ArrayQuangCao = new ArrayList<>();
        ArrayQuangCao.add(new SlideModel("https://treobangron.com.vn/wp-content/uploads/2022/09/banner-khuyen-mai-42.jpg", null));
        ArrayQuangCao.add(new SlideModel("https://treobangron.com.vn/wp-content/uploads/2022/09/banner-khuyen-mai-40.jpg", null));
        ArrayQuangCao.add(new SlideModel("https://treobangron.com.vn/wp-content/uploads/2022/09/banner-khuyen-mai-23.jpg", null));
        imageSlider.setImageList(ArrayQuangCao, ScaleTypes.CENTER_CROP);
        imageSlider.setOnTouchListener(touchListener);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        private float startX;
        private float startY;
        private long startTime;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    startTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float endY = event.getY();
                    long endTime = System.currentTimeMillis();

                    float deltaX = endX - startX;
                    float deltaY = endY - startY;
                    long deltaTime = endTime - startTime;

                    if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 100 && deltaTime < 200) {
                        if (deltaX > 0) {
                            imageSlider.startSliding(10000);
                        } else {
                            imageSlider.startSliding(10000);
                        }
                    }
                    break;
            }
            return true;
        }
    };

    private void Anhxa(View rootView) {
        toolBar = rootView.findViewById(R.id.toolBarHomePage_HomeFragment);
        recyclerView = rootView.findViewById(R.id.recyclerViewHomePage_HomeFragMent);
        navigationView = rootView.findViewById(R.id.navigationHomePage_HomeFragMent);
        listView = rootView.findViewById(R.id.listViewHomePage_HomeFragMent);
        drawerLayout = rootView.findViewById(R.id.drawerLayoutHomePage_HomeFragMent);
        imageSlider = rootView.findViewById(R.id.imageSliderHomePage_HomeFragMent);
        searchView = rootView.findViewById(R.id.searchHomePage_HomeFragment);
        mangSanPhamMoi = new ArrayList<>();
        spMoiAdapter = new SPMoiAdapter(requireActivity().getApplicationContext(), mangSanPhamMoi);
        listView.setAdapter(spMoiAdapter);
        mangLoaiSp = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }
    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSPModel -> {
                            if (loaiSPModel.isSuccess()) {
//                                Toast.makeText(getApplicationContext(),loaiSPModel.getResult().get(0).getName(), Toast.LENGTH_LONG).show();
                                mangLoaiSp = loaiSPModel.getResult();
                                loaispAdapter = new Loaisp_Adapter(getActivity().getApplicationContext(), mangLoaiSp);
                                ListView listView = getActivity().findViewById(R.id.listViewHomePage_HomeFragMent);
                                listView.setAdapter(loaispAdapter);
                            }
                        }
                ));
    }
}



