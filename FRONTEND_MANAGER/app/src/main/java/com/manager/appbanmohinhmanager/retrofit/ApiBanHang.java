package com.manager.appbanmohinhmanager.retrofit;


import com.manager.appbanmohinhmanager.model.CouponModel;
import com.manager.appbanmohinhmanager.model.MessageModel;
import com.manager.appbanmohinhmanager.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiBanHang {
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

    @POST("getcoupon_manager.php")
    @FormUrlEncoded
    Observable<CouponModel> getCoupon_Manager(
            @Field("type") String type,
            @Field("isPublic") int isPublic
    );

    @POST("addvoucher.php")
    @FormUrlEncoded
    Observable<MessageModel> addVoucher(
            @Field("name") String name,
            @Field("count") int count,
            @Field("discount") int discount,
            @Field("dateFrom") String dateFrom,
            @Field("dateEnd") String dateEnd,
            @Field("userId") int userId,
            @Field("isPublic") int isPublic
    );

    @POST("deletevoucher.php")
    @FormUrlEncoded
    Observable<MessageModel> deleteVoucher(
            @Field("id") int id
    );
}
