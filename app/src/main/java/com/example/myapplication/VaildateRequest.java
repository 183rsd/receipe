package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class VaildateRequest extends StringRequest {
    // 서버 url 설정
    final static private String URL = "https://restserver-lzssy.run.goorm.io/users/signup";
    private final Map<String, String> map;

    public VaildateRequest(String user_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", user_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
