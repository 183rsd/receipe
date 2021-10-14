package com.example.myapplication;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyAPI {
    @POST("/users/login") // 로그인 post
    Call<loginResponse> post_posts(@Body loginData post);

    @POST("/users/sign2") // 아이디 중복확인을 위한 회원가입 post
    Call<loginData> post_sign(@Body loginData post);

    @Multipart // retrofit2으로 파일을 전송할 때는 Multipart를 써야함
    @POST("/picture/predict/") // 사진 post
    Call<pictureData> post_picture(@PartMap HashMap<String, RequestBody> data, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @PATCH("/users/{pk}") // 비밀번호 변경을 위한 patch
    Call<loginData> patch_pw(@Path("pk") int pk, @Field("password") String password);

    @DELETE("/users/{pk}") // 계정 삭제를 위한 delete
    Call<loginData> delete_posts(@Path("pk") int pk);


    @GET("/users/sign")
    Call<loginData> get_user_id(@Query("user_id") String user_id);

    @GET("/users/{pk}")
    Call<List<loginData>> get_post_pk(@Path("pk") int pk);

    @GET("/recipe/detail/{pic_list}/")
    Call<pictureData> get_rec_name(@Path(value = "pic_list", encoded = false) String pic_list);

//    @GET("/recipe/detail/{pk}")


}
