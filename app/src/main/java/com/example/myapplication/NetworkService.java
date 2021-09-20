package com.example.myapplication;

import java.util.List;

import okhttp3.internal.Version;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {
    @POST("/users/login")
    Call<Login> post_login(@Body Login login);

    @PATCH("/users/login/{pk}")
    Call<Login> patch_version(@Path("pk") int pk, @Body Login login);

    @DELETE("/users/login/{pk}")
    Call<Login> delete_version(@Path("pk") int pk);

    @GET("/users/login")
    Call<List<Login>> get_version();

    @GET("/users/login/{pk}")
    Call<Login> get_pk_version(@Path("pk") int pk);

//    @POST("/api/restaurants/")
//    Call<Restaurant> post_restaruant(@Body Restaurant restaurant);
//
//    @PATCH("/api/restaurants/{pk}/")
//    Call<Restaurant> patch_restaruant(@Path("pk") int pk, @Body Restaurant restaurant);
//
//    @DELETE("/api/restaurants/{pk}/")
//    Call<Restaurant> delete_restaruant(@Path("pk") int pk);
//
//    @GET("/api/restaurants/")
//    Call<List<Restaurant>> get_restaruant();
//
//    @GET("/api/restaurants/{pk}/")
//    Call<Restaurant> get_pk_restaruant(@Path("pk") int pk);
//
//    @GET("/api/weathers/{pk}/restaurant_list/")
//    Call<List<Restaurant>> get_weather_pk_restaruant(@Path("pk") int pk);
}
