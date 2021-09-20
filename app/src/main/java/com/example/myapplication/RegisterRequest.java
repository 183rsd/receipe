package com.example.myapplication;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{
    private final static String URL = "https://restserver-lzssy.run.goorm.io/users/signup";
    private final Map<String, String> map;

    public RegisterRequest(String user_id, String password,  int age, int sex, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", user_id);
        map.put("password", password);
        map.put("age", age+"");
        map.put("sex", sex+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
