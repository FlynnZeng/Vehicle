package com.me.vehicle.api;

import android.content.Context;

import com.me.vehicle.utils.PreferencesUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;

import java.security.cert.X509Certificate;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.205.220:8080/";
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            // 创建忽略证书验证的 OkHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        String token = PreferencesUtil.getString(context, "token");
                        Request.Builder builder = chain.request().newBuilder();

                        if (token != null && !token.isEmpty()) {
                            builder.addHeader("Authorization", token);
                        }

                        Request request = builder.build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
