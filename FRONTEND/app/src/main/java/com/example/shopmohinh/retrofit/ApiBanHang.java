package com.example.shopmohinh.retrofit;
import com.example.shopmohinh.model.CartModel;
import com.example.shopmohinh.model.MessageModel;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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


    @POST("update_profile.php")
    @FormUrlEncoded
    Observable<MessageModel> updateProfile(
            @Field("userId") int id,
            @Field("userName") String name,
            @Field("userPhone") String phone,
            @Field("userAddress") String address
            );

    @POST("shopping_cart.php")
    @FormUrlEncoded
    Observable<MessageModel> shoppingCart(
            @Field("accountId") int accountId,
            @Field("productId") int productId,
            @Field("quantity") int quantity
    );

    @POST("get_shopping_cart.php")
    @FormUrlEncoded
    Observable<CartModel> getShoppingCart(
            @Field("accountId") int accountId
    );

    @POST("update_cart.php")
    @FormUrlEncoded
    Observable<MessageModel> updateShoppingCart(
            @Field("accountId") int accountId,
            @Field("productId") int productId,
            @Field("quantity") int quantity
    );

    @POST("delete_cart.php")
    @FormUrlEncoded
    Observable<MessageModel> deleteShoppingCart(
            @Field("accountId") int accountId
    );
  
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
