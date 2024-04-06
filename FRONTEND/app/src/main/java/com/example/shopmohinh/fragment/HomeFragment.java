package com.example.shopmohinh.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shopmohinh.R;

import com.example.shopmohinh.activity.CheckInActivity;
import com.example.shopmohinh.activity.CouponActivity;
import com.example.shopmohinh.adapter.SPMoiAdapter;
import com.example.shopmohinh.activity.MiniGameActivity;
import com.example.shopmohinh.activity.SpinCouponActivity;
import com.example.shopmohinh.model.LoaiSP;
import com.example.shopmohinh.model.Product;
import com.example.shopmohinh.model.SanPhamMoi;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import org.objectweb.asm.Handle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    ImageSlider imageSlider;
    SearchView searchView;
    List<Product> mangSanPhamMoi;
    SPMoiAdapter spMoiAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    int page = 1;
    List<LoaiSP> mangLoaiSp;
    CardView cardWheel_Coupon, cardMiniGame, cardCheckIn;

    ImageView imageCouponHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Anhxa(rootView);
        ActionViewFlipper();
        getSanPhamMoi(page);
        addEventLoad();
        initControll();
        return rootView;
    }

    private void addEventLoad(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false){
//                    Toast.makeText(getActivity(),String.valueOf(gridLayoutManager.findLastCompletelyVisibleItemPosition() == mangSanPhamMoi.size()-1), Toast.LENGTH_LONG).show();
                    if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == mangSanPhamMoi.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }
    private void loadMore(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Đang tải...", Toast.LENGTH_SHORT).show();
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = page + 1;
                getSanPhamMoi(page);
                isLoading = false;
//                spMoiAdapter.notifyDataSetChanged();
            }
        },2500);
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
        cardCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckInActivity.class);
                startActivity(intent);
            }
        });
        imageCouponHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CouponActivity.class);
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
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        drawerLayout = rootView.findViewById(R.id.drawerLayoutHomePage_HomeFragMent);
        imageSlider = rootView.findViewById(R.id.imageSliderHomePage_HomeFragMent);
        searchView = rootView.findViewById(R.id.searchHomePage);
        mangSanPhamMoi = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        cardWheel_Coupon = rootView.findViewById(R.id.cardWheel_coupon);
        cardMiniGame = rootView.findViewById(R.id.cardMiniGame);
        cardCheckIn = rootView.findViewById(R.id.cardCheckIn_HomeFragment);
        imageCouponHome = rootView.findViewById(R.id.imageCoupon_HomeFragment);
    }

    private void getSanPhamMoi(int page) {
        compositeDisposable.add(apiBanHang.getSanPhamMoi(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                if (spMoiAdapter == null){
                                    mangSanPhamMoi = sanPhamMoiModel.getResult();
                                    spMoiAdapter = new SPMoiAdapter(getContext(), mangSanPhamMoi);
                                    recyclerView.setAdapter(spMoiAdapter);
                                }else {
                                    int position = mangSanPhamMoi.size()-1;
                                    int soluongadd = sanPhamMoiModel.getResult().size();
                                    for (int i=0; i<soluongadd; i++){
                                        mangSanPhamMoi.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    spMoiAdapter.notifyItemRangeInserted(position, soluongadd);
                                }
                            }
                            else {
//                                int position = mangSanPhamMoi.size()-1;
//                                spMoiAdapter.notifyItemRemoved(position);
                                Toast.makeText(getActivity(),"Đã hết sản phẩm", Toast.LENGTH_LONG).show();
                            }
                        },throwable -> {
                            Toast.makeText(getActivity(),throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }

                ));
        
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}



