package com.manager.appbanmohinhmanager.retrofit;

import com.manager.appbanmohinhmanager.model.CategoryManagerModel;
import com.manager.appbanmohinhmanager.model.ProductManagerModel;
import com.manager.appbanmohinhmanager.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

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
}
