package com.example.shopmohinh.retrofit;

import com.example.shopmohinh.model.MessageModel;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel> createOrder(
            @Field("email") String email,
            @Field("phone") String sdt,
            @Field("total") String tongtien,
            @Field("accountId") int id,
            @Field("address") String diachi,
            @Field("chitiet") String chitiet
    );

    @POST("updatezalo.php")
    @FormUrlEncoded
    Observable<MessageModel> updateZalo(
            @Field("iddonhang") int id,
            @Field("token") String token
    );
}
