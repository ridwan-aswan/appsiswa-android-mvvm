package com.ridwanrsup94atgmaildotcom.appsiswa.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ridwanrsup94atgmaildotcom.appsiswa.api.ApiServer.BASE_URL;

/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */
public class APIClient { ;


    private static Retrofit retrofit = null;
    public  static Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;


    }
}
