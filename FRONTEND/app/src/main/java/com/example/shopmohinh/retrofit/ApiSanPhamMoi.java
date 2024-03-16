package com.example.shopmohinh.retrofit;

import android.database.Observable;

import com.example.shopmohinh.model.SanPhamModel;

import retrofit2.http.GET;

public interface ApiSanPhamMoi {
    @GET("getsanphammoi.php")
    Observable<SanPhamModel> getSanPhamMoi();
}
