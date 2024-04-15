package com.example.shopmohinh.zalo;

import com.example.shopmohinh.zalo.AppInfo;
import com.example.shopmohinh.zalo.Helpers;
import com.example.shopmohinh.zalo.HttpProvider;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GetOrder {
    private class OrderData {
        String AppId;
        String AppTransId;
        String Mac;

        private OrderData(String appTransId) throws Exception {

            AppId = String.valueOf(AppInfo.APP_ID);;
            AppTransId = appTransId;

            String inputHMac = String.format("%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    AppInfo.MAC_KEY);

            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);
        }
    }

    public JSONObject GetOrderApi(String appTransId) throws Exception {
        OrderData input = new OrderData(appTransId);

        RequestBody formBody = new FormBody.Builder()
                .add("app_id", input.AppId)
                .add("app_trans_id", input.AppTransId)
                .add("mac", input.Mac)
                .build();

        JSONObject data = HttpProvider.sendPost(AppInfo.URL_GET_ORDER, formBody);
        return data;
    }
}