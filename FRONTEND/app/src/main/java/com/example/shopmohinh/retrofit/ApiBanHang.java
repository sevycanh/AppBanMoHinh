package com.example.shopmohinh.retrofit;

import com.example.shopmohinh.model.CartModel;
import com.example.shopmohinh.model.CouponModel;
import com.example.shopmohinh.model.InforDetailModel;
import com.example.shopmohinh.model.ItemOrderDetailModel;
import com.example.shopmohinh.model.MessageModel;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.shopmohinh.model.LoaiSPModel;
import com.example.shopmohinh.model.MessageModel;
import com.example.shopmohinh.model.Order;
import com.example.shopmohinh.model.OrderModel;
import com.example.shopmohinh.model.SanPhamMoiModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

import com.example.shopmohinh.model.SanPhamSearchModel;
import com.example.shopmohinh.model.UserModel;

import java.sql.Date;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiBanHang {
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel> createOrder(
            @Field("username") String username,
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
            @Field("accountId") int id,
            @Field("username") String name,
            @Field("phone") String phone,
            @Field("address") String address
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

    @POST("getcoin.php")
    @FormUrlEncoded
    Observable<UserModel> getCoin(
            @Field("id") int id
    );

    @POST("updatecoin.php")
    @FormUrlEncoded
    Observable<MessageModel> updateCoin(
            @Field("id") int id,
            @Field("coin") int coin
    );

    @POST("update_luckybox.php")
    @FormUrlEncoded
    Observable<MessageModel> updateLuckyBox(
            @Field("id") int id,
            @Field("coin") int coin,
            @Field("luckybox") int luckybox
    );

    @POST("update_checkin.php")
    @FormUrlEncoded
    Observable<MessageModel> updateCheckIn(
            @Field("id") int id,
            @Field("coin") int coin,
            @Field("checkin") int checkin
    );

    @POST("insertcoupon.php")
    @FormUrlEncoded
    Observable<CouponModel> insertCoupon(
            @Field("name") String name,
//            @Field("code") String code,
            @Field("count") int count,
            @Field("discount") int discount,
//            @Field("dateFrom") Date dateFrom,
//            @Field("dateTo") Date dateTo,
            @Field("userId") int userId,
            @Field("isPublic") int isPublic,
            @Field("duration") int duration
    );

    @POST("check_in.php")
    @FormUrlEncoded
    Observable<MessageModel> checkIn(
            @Field("id") int id
    );

    @POST("dangnhapgoogle.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhapGoogle(
            @Field("email") String email
    );

    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();


    @POST("checkQuantityProduct.php")
    @FormUrlEncoded
    Observable<MessageModel> checkQuantityProduct(
            @Field("productId") int productId
    );

    @POST("getsanphammoi.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPhamMoi(
            @Field("page") int page
    );

    @POST("searchsp.php")
    @FormUrlEncoded
    Observable<SanPhamSearchModel> searchSp(
            @Field("type") String type,
            @Field("tensp") String tensp
    );

    @GET("getOrder.php")
    Observable<OrderModel> getOrder(@Query("accountId") int accountID);

    @GET("getOrderByOrderStatus.php")
    Observable<OrderModel> getOrderByOrderStatus(@Query("accountId") int accountID, @Query("order_status") int order_status);


    @GET("xemdonhang.php")
    Observable<InforDetailModel> getInforDetail(@Query("orderId") int orderID);

    @GET("getItemOrderDetail.php")
    Observable<ItemOrderDetailModel> getItemOrderDetail(@Query("orderId") int orderID);

    @POST("updateOrderStatus.php")
    @FormUrlEncoded
    Observable<OrderModel> updateOrderStatus(@Field("orderId") int orderID);
}
