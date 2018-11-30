package com.bluebuddy.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient2 {

    private static Retrofit retrofit2 = null;

    public static Retrofit getClient2() {
        if (retrofit2==null) {
            //
           /* Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();*/
            //
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            retrofit2 = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl("https://fcm.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit2;
    }
}
