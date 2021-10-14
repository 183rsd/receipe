package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.ResponseBody;
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
    private Context mContext;

    // 뒤로가기
    private BackPressHandler backPressHandler = new BackPressHandler(MainActivity.this);


    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    public static String user_id,password;
    private AlertDialog.Builder dialog;
    private CheckBox autoLogin_check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, LoadingActivity.class);
//        startActivity(intent);


        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        autoLogin_check = findViewById(R.id.autoLogin_check);
        mContext = this;

        initMyAPI(BASE_URL);


        boolean boo = PreferenceManager.getBoolean(mContext,"check"); // 로그인 정보 기억하기 체크 유무 확인
        if(boo){ // 로그인 정보 기억하기 버튼이 체크되어 있으면
            // PreferencceManager에 저장된 id,pw값을 editText에 세팅
            et_id.setText(PreferenceManager.getString(mContext,"user_id"));
            et_pass.setText(PreferenceManager.getString(mContext,"password"));
            autoLogin_check.setChecked(true);
        }



        // 로그인버튼 클릭시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력한 id,pw를 user_id, password에 저장
                String user_id = et_id.getText().toString();
                String password = et_pass.getText().toString();
                loginData data = new loginData(user_id,password);

                // user_id, password 값을 PreferenceManager에 저장 (자동로그인 데이터 저장)
                PreferenceManager.setString(mContext,"user_id",user_id);
                PreferenceManager.setString(mContext,"password",password);

                // 저장한 키 값으로 저장된 아이디, 비밀번호를 불러와서 check_user_id, check_password에 저장
                PreferenceManager.getString(mContext,"user_id");
                PreferenceManager.getString(mContext,"password");

                Call<loginResponse> loginCall = mMyAPI.post_posts(data);
                loginCall.enqueue(new Callback<loginResponse>() { // post 전송
                    @Override
                    public void onResponse(Call<loginResponse> call, Response<loginResponse> response) {
                        if(response.isSuccessful()){

                            loginResponse result = response.body();
                            int id = result.getId();
                            String user_id = result.getUser_id();
                            String password = result.getPassword();
                            int age = result.getAge();
                            int sex = result.getSex();
                            Toast.makeText(MainActivity.this,user_id +"님 환영합니다.",Toast.LENGTH_SHORT).show();

                            if(autoLogin_check.isChecked()){
                                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLoginEdit = auto.edit();
                                autoLoginEdit.putString("user_id",user_id);
                                autoLoginEdit.putString("password",password);
                                autoLoginEdit.putInt("age",age);
                                autoLoginEdit.putInt("sex",sex);
                                autoLoginEdit.commit();

                            }

                            Intent intent1 = new Intent(getApplicationContext(),MainActivity2.class);
                            intent1.putExtra("id",id);
                            intent1.putExtra("user_id", user_id);
                            intent1.putExtra("password", password);
                            intent1.putExtra("age", age);
                            intent1.putExtra("sex", sex);
                            startActivity(intent1);


                        } else{
                            Toast.makeText(MainActivity.this,"아이디 또는 비밀번호가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Status Code : " + response.code());
                            Log.d(TAG,response.errorBody().toString());
                            Log.d(TAG,call.request().body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<loginResponse> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                        Toast.makeText(MainActivity.this,"서버 오류",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 자동로그인 체크박스 유무에 따른 동작 구현
        autoLogin_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){ // 체크박스 체크되어있으면
                    // editText에서 아이디, 비밀번호 가져와서 PreferenceManager에 저장
                    PreferenceManager.setString(mContext,"user_id",et_id.getText().toString());
                    PreferenceManager.setString(mContext,"password",et_pass.getText().toString());
                    PreferenceManager.setBoolean(mContext,"check",autoLogin_check.isChecked()); // 현재 체크박스 상태 저장
                } else{ // 체크박스 해제되어있으면
                    PreferenceManager.setBoolean(mContext,"check",autoLogin_check.isChecked()); // 현재 체크박스 상태 저장
                    PreferenceManager.clear(mContext); // 로그인 정보 모두 날림
                }
            }
        });


//         회원가입 버튼 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed(){
//        // Default
//        backPressHandler.onBackPressed();
//        // Toast 메세지 사용자 지정
//        backPressHandler.onBackPressed("'뒤로' 버튼 한번 더 누르시면 종료됩니다.");
//        // 뒤로가기 간격 사용자 지정
//        backPressHandler.onBackPressed(3000);
//        // Toast, 간격 사용자 지정
       backPressHandler.onBackPressed("'뒤로' 버튼 한번 더 누르시면 종료됩니다.",3000);
    }

    // 키보드 숨기기
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_id.getWindowToken(),0);
        imm.hideSoftInputFromWindow(et_pass.getWindowToken(),0);
    }

    // 화면 터치 시 키보드 내려감
    public boolean dispatchTouchEvent(MotionEvent ev){
        View focusView = getCurrentFocus();
        if(focusView != null){
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if(!rect.contains(x,y)){
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }



}