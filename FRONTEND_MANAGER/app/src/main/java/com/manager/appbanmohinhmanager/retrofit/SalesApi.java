package com.manager.appbanmohinhmanager.retrofit;
import com.manager.appbanmohinhmanager.model.ProductManagerModel;
import com.manager.appbanmohinhmanager.model.StatisticalModel;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SalesApi {
    @GET("statistical.php")
    Observable<StatisticalModel> getDataChart();

    @GET("statisticalByMonth.php")
    Observable<StatisticalModel> getStatisticalByMonth();
}
