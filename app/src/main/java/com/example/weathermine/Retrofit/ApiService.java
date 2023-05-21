package com.example.weathermine.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static String BASE_URL = "https://api.open-meteo.com/v1/";
    public static ApiEndpoint endpoint() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiEndpoint.class);
    }
}
