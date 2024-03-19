package com.example.shopmohinh.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shopmohinh.R;

import com.example.shopmohinh.adapter.SPMoiAdapter;
import com.example.shopmohinh.model.LoaiSP;
import com.example.shopmohinh.model.SanPhamMoi;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.Utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageSlider imageSlider;
    SearchView searchView;
    SPMoiAdapter spMoiAdapter;
    List<SanPhamMoi> mangSanPhamMoi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<LoaiSP> mangLoaiSp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Anhxa(rootView);
        ActionViewFlipper();
        getSanPhamMoi();
        return rootView;
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
        recyclerView = rootView.findViewById(R.id.recyclerViewHomePage_HomeFragMent);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        drawerLayout = rootView.findViewById(R.id.drawerLayoutHomePage_HomeFragMent);
        imageSlider = rootView.findViewById(R.id.imageSliderHomePage_HomeFragMent);
        searchView = rootView.findViewById(R.id.searchHomePage);
        mangSanPhamMoi = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    private void getSanPhamMoi() {
        compositeDisposable.add(apiBanHang.getSanPhamMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                mangSanPhamMoi = sanPhamMoiModel.getResult();
                                spMoiAdapter = new SPMoiAdapter(getContext(), mangSanPhamMoi);
                                recyclerView.setAdapter(spMoiAdapter);
                            }
                        },throwable -> {
                            Toast.makeText(getActivity(),throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }

                ));
    }
}



