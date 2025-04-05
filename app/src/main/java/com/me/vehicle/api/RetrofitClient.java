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

    private static final String BASE_URL = "https://frp-fox.com:40417/";
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
                    .sslSocketFactory(createSSLSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true) // 忽略主机名验证
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // 创建信任所有证书的 SSLSocketFactory
    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 创建信任所有证书的 TrustManager
    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }
    };
}
