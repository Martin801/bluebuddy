package com.bluebuddy.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // server link live
//    public static final String BASE_URL = "http://67.225.128.57/bluebuddyapi/ios/";

    // server link live
    public static final String BASE_URL = "http://67.225.128.57/bluebuddyapi/demo/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(300, TimeUnit.SECONDS);
            httpClient.readTimeout(300, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
//    public static Retrofit getClient2() {
//        if (retrofit==null) {
//            //
//            Gson gson = new GsonBuilder()
//                    .setLenient()
//                    .create();
//            //
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            httpClient.addInterceptor(logging);
//
//            retrofit = new Retrofit.Builder()
//                    .client(httpClient.build())
//                    .baseUrl("https://fcm.googleapis.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//
//        return retrofit;
//    }
}
