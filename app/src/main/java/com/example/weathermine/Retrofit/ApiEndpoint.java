package com.example.weathermine.Retrofit;

import retrofit2.Call;
import com.example.weathermine.WeatherModelRetrofit;
import retrofit2.http.GET;

public interface ApiEndpoint {
    @GET("forecast?latitude=-7.98&longitude=112.63&daily=weathercode&current_weather=true&timezone=auto")
    Call<WeatherModelRetrofit> getData();
}
