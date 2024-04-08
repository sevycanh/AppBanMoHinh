package com.manager.appbanmohinhmanager.retrofit;

import com.manager.appbanmohinhmanager.model.NotiResponse;
import com.manager.appbanmohinhmanager.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAvPCN4aI:APA91bEqZK7AFyEDWQmiHnHl8mzb1C5PYBplwTLlLeQJRYvBQI7CJWLhsqzym41h7OBYr57m-2lIoRrGpYv5USULGJ7e6L6KAUDXd83DZJvMx9H2B38fWqp_TL_XGRdWSW0jIoJs-GYb"
    })
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSendData data);
}
