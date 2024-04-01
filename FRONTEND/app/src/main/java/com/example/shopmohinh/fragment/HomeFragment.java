package com.example.shopmohinh.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shopmohinh.R;

import com.example.shopmohinh.activity.MiniGameActivity;
import com.example.shopmohinh.activity.SpinCouponActivity;
import com.example.shopmohinh.model.LoaiSP;
import com.example.shopmohinh.model.SanPhamMoi;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    ImageSlider imageSlider;
    List<SanPhamMoi> mangSanPhamMoi;
    ApiBanHang apiBanHang;
    List<LoaiSP> mangLoaiSp;
    CardView cardWheel_Coupon, cardMiniGame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Anhxa(rootView);
        ActionViewFlipper();
        initControll();
        return rootView;
    }

    private void initControll() {
        cardWheel_Coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpinCouponActivity.class);
                startActivity(intent);
            }
        });

        cardMiniGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MiniGameActivity.class);
                startActivity(intent);
            }
        });
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
        drawerLayout = rootView.findViewById(R.id.drawerLayoutHomePage_HomeFragMent);
        imageSlider = rootView.findViewById(R.id.imageSliderHomePage_HomeFragMent);
        mangSanPhamMoi = new ArrayList<>();
        mangLoaiSp = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        cardWheel_Coupon = rootView.findViewById(R.id.cardWheel_coupon);
        cardMiniGame = rootView.findViewById(R.id.cardMiniGame);
    }
}



