package com.example.shopmohinh.address;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class HttpProvider {
    public static JSONObject sendGet(String URL) {
        JSONObject data = null;
        try {
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectionSpecs(Collections.singletonList(spec))
                    .callTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(URL)
                    .addHeader("Content-Type", "application/json")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                Log.println(Log.ERROR, "BAD_REQUEST", response.body().string());
            } else {
                String responseData = response.body().string();
                data = new JSONObject(responseData);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}