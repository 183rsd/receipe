package com.example.myapplication;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest{
    // 서버 url 설정 (php 파일 연동)
    private static final String URL = "https://restserver-lzssy.run.goorm.io/users/login";  // http://자신의주소ip/login.php

    private final Map<String, String> map;


    public LoginRequest(String user_id, String password, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", user_id);
        map.put("userPassword", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }

}
