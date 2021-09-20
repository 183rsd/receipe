package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyAPI {
    @POST("/users/login")
    Call<loginData> post_posts(@Body loginData post);

    @PATCH("/users/login/{pk}")
    Call<loginData> patch_posts(@Path("pk") int pk, @Body loginData post);

    @DELETE("/users/login/{pk}")
    Call<loginData> delete_posts(@Path("pk") int pk);

    @GET("/users/login")
    Call<List<loginData>> get_posts();

    @GET("/users/login/{pk}")
    Call<loginData> get_post_pk(@Path("pk") int pk);
}
