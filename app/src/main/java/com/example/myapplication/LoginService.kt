//package com.example.myapplication
//
//import retrofit2.Call
//import retrofit2.http.Field
//import retrofit2.http.FormUrlEncoded
//import retrofit2.http.POST
//
//interface LoginService {
//
//    @FormUrlEncoded
//    @POST("/users/login")
//    fun requestLogin(
//        // 인풋 정의하는 곳
//        @Field("user_id") user_id : String,
//        @Field("password") password : String
//    ) : Call<Login> // 아웃풋 정의하는 곳
//}