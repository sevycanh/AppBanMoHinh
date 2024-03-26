package com.example.shopmohinh.retrofit;

import com.example.shopmohinh.model.CategoryModel;
import com.example.shopmohinh.model.ProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SalesApi {
    @GET("category.php")
    Observable<CategoryModel> getCategory();

    @FormUrlEncoded
    @POST("product.php")
    Observable<ProductModel> getProduct(@Field("page") int page , @Field("id_category") int category);
}
