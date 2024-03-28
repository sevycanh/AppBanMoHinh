package com.example.shopmohinh.retrofit;


import com.example.shopmohinh.model.LoaiSPModel;
import com.example.shopmohinh.model.SanPhamMoiModel;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

import com.example.shopmohinh.model.SanPhamSearchModel;
import com.example.shopmohinh.model.UserModel;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiBanHang {
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangKy(
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String pass
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();

    @POST("getsanphammoi.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPhamMoi(
            @Field("page") int page
    );

    @POST("searchsp.php")
    @FormUrlEncoded
    Observable<SanPhamSearchModel> searchSp (
            @Field("type") String type,
            @Field("tensp") String tensp
    );
}
