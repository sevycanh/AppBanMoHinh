package com.example.shopmohinh.retrofit;


import com.example.shopmohinh.model.LoaiSPModel;
import com.example.shopmohinh.model.MessageModel;
import com.example.shopmohinh.model.SanPhamMoiModel;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import com.example.shopmohinh.model.UserModel;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiBanHang {
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangKy(
            @Field("email") String email
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email
    );

    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token
    );
  @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();
    @GET("getsanphammoi.php")
    Observable<SanPhamMoiModel> getSanPhamMoi();
}
