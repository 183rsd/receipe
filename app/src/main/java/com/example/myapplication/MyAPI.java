package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyAPI {
    @POST("/users/login")
    Call<loginResponse> post_posts(@Body loginData post);

    @POST("/users/sign2")
    Call<loginData> post_sign(@Body loginData post);

    @FormUrlEncoded
    @PATCH("/users/{pk}")
    Call<loginData> patch_pw(@Path("pk") int pk, @Field("password") String password);

    @DELETE("/users/{pk}")
    Call<loginData> delete_posts(@Path("pk") int pk);


    @GET("/users/sign")
    Call<loginData> get_user_id(@Query("user_id") String user_id);

    @GET("/users/{pk}")
    Call<List<loginData>> get_post_pk(@Path("pk") int pk);
}
