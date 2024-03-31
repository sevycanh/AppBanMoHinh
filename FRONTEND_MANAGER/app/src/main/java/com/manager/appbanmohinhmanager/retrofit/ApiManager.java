package com.manager.appbanmohinhmanager.retrofit;

import com.manager.appbanmohinhmanager.model.ProductManagerModel;
import com.manager.appbanmohinhmanager.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiManager {

    @GET("getproduct.php")
    Observable<ProductManagerModel> getDataProduct();
}
