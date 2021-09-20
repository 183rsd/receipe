package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    // 서버 url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;


    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    public static String user_id,password;
    private AlertDialog.Builder dialog;
    Login login = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        initMyAPI(BASE_URL);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginData loginData = new loginData();
                user_id = et_id.getText().toString();
                password = et_pass.getText().toString();
                Call<loginData> loginCall = mMyAPI.post_posts(loginData);
                loginCall.enqueue(new Callback<loginData>() {
                    @Override
                    public void onResponse(Call<loginData> call, Response<loginData> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"POST 성공");
                        } else{
                            Log.d(TAG,"Status Code : " + response.code());
                            Log.d(TAG,response.errorBody().toString());
                            Log.d(TAG,call.request().body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.example.myapplication.loginData> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
            }
        });

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://restserver-lzssy.run.goorm.io") // 장고 서버 주소 작성
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//
//        LoginService loginService = retrofit.create(LoginService.class);


        // 회원가입 버튼 클릭 시 수행
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

        // 로그인 버튼 클릭 시 수행
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String user_id = et_id.getText().toString();
//                String password = et_pass.getText().toString();
//
//                loginService.requestLogin(user_id, password).enqueue(new Callback<Login>() {
//                    @Override
//                    public void onFailure(Call<Login> call, Throwable t) {
//                        // 웹 통신 실패했을 때
//                        Log.d("실패", "onFailure: " + t.toString()); //서버와 연결 실패
//                    }
//
//                    @Override
//                    public void onResponse(Call<Login> call, Response<Login> response) {
//                        // 웹 통신 성공했을 때 ( 응답값 받아옴 )
//
//                        login = response.body(); // code, msg
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        dialog = builder.setMessage("성공");
//                        dialog.show();
//
//                    }
//
//
//                });
//            }
//        });
    }
    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

//    public void onClick(View v){
//        if( v == btn_login){
//            loginData loginData = new loginData();
//            user_id = et_id.getText().toString();
//            password = et_pass.getText().toString();
//            Call<loginData> loginCall = mMyAPI.post_posts(loginData);
//            loginCall.enqueue(new Callback<loginData>() {
//                @Override
//                public void onResponse(Call<loginData> call, Response<loginData> response) {
//                    if(response.isSuccessful()){
//                        Log.d(TAG,"POST 성공");
//                    } else{
//                        Log.d(TAG,"Status Code : " + response.code());
//                        Log.d(TAG,response.errorBody().toString());
//                        Log.d(TAG,call.request().body().toString());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<com.example.myapplication.loginData> call, Throwable t) {
//                    Log.d(TAG,"Fail msg : " + t.getMessage());
//                }
//            });
//        } else if( v == btn_register){
//
//        }
//    }
}