package com.example.shopmohinh.retrofit;

import com.example.shopmohinh.model.LoaiSPModel;
import com.example.shopmohinh.model.SanPhamMoiModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();
    @GET("getsanphammoi.php")
    Observable<SanPhamMoiModel> getSanPhamMoi();
}
