package com.manager.appbanmohinhmanager.retrofit;

import com.manager.appbanmohinhmanager.model.Account;
import com.manager.appbanmohinhmanager.model.AccountModel;
import com.manager.appbanmohinhmanager.model.CategoryManagerModel;
import com.manager.appbanmohinhmanager.model.InforDetailManagerModel;
import com.manager.appbanmohinhmanager.model.ItemOrderDetailManagerModel;
import com.manager.appbanmohinhmanager.model.OrderManagerModel;
import com.manager.appbanmohinhmanager.model.ProductManagerModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiManager {

    @GET("getproduct.php")
    Observable<ProductManagerModel> getDataProduct();

    @POST("addproduct.php")
    @FormUrlEncoded
    Observable<ProductManagerModel> addDataProduct(
            @Field("name") String name,
            @Field("price") int price,
            @Field("quantity") int quantity,
            @Field("description") String description,
            @Field("main_image") String main_image,
            @Field("sub_image") String sub_image,
            @Field("coupon") int coupon,
            @Field("category_id") int category_id,
            @Field("status") int status
    );

    @POST("updateproduct.php")
    @FormUrlEncoded
    Observable<ProductManagerModel> updateDataProduct(
            @Field("id") int id,
            @Field("name") String name,
            @Field("price") int price,
            @Field("quantity") int quantity,
            @Field("description") String description,
            @Field("main_image") String main_image,
            @Field("sub_image") String sub_image,
            @Field("coupon") int coupon,
            @Field("category_id") int category_id,
            @Field("status") int status
    );

    @POST("deleteproduct.php")
    @FormUrlEncoded
    Observable<ProductManagerModel> deleteProduct(
            @Field("id") int id
    );

    @GET("getcategory.php")
    Observable<CategoryManagerModel> getDataCategory();

    @POST("addcategory.php")
    @FormUrlEncoded
    Observable<CategoryManagerModel> addDataCategory(
            @Field("namecategory") String name,
            @Field("imgcategory") String img
    );

    @POST("updatecategory.php")
    @FormUrlEncoded
    Observable<CategoryManagerModel> updateDataCategory(
            @Field("idcategory") int id,
            @Field("namecategory") String name,
            @Field("imgcategory") String img
    );

    @POST("deletecategory.php")
    @FormUrlEncoded
    Observable<CategoryManagerModel> deleteDataCategory(
            @Field("idcategory") int id
    );

    @POST("hiddenproduct.php")
    @FormUrlEncoded
    Observable<ProductManagerModel> hiddenProduct(
            @Field("idcategory") int id
    );

    @GET("getOrderInOrderManager.php")
    Observable<OrderManagerModel> getOrderInOrderManager();

    @GET("xemdonhang.php")
    Observable<InforDetailManagerModel> getInforDetail(@Query("orderId") int orderID);

    @POST("updateOrderStatusManger.php")
    @FormUrlEncoded
    Observable<InforDetailManagerModel> updateOrderStatusManager(@Field("orderId") int orderID, @Field("order_status") int order_status);

    @GET("getItemOrderDetail.php")
    Observable<ItemOrderDetailManagerModel> getItemOrderDetail(@Query("orderId") int orderID);

    @GET("searchOrderbyID.php")
    Observable<OrderManagerModel> searchOrderbyID(@Query("order_status") int order_status, @Query("order_id") String order_id);

    @POST("getAccountManager.php")
    @FormUrlEncoded
    Observable<AccountModel> getAccountManager(@Field("status") int status);

    @POST("updateAccountStatus.php")
    @FormUrlEncoded
    Observable<AccountModel> updateAccountStatus(@Field("status") int status, @Field("accountid") int accountid);
}
