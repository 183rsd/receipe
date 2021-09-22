package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_age;
    private int user_sex;
    private MyAPI mMyAPI;
    private RadioGroup radioGroup;
    private Button btn_register, btn_check, btn_cancle_regi;
    private AlertDialog dialog;
    private boolean validate = false;

    private final String TAG = getClass().getSimpleName();
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        radioGroup = findViewById(R.id.rg_group);
        et_age = findViewById(R.id.et_age);


        // 아이디 중복 체크
        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = et_id.getText().toString();
                if(validate){
                    return; // 검증완료
                }
                if(user_id.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인",null).create();
                                dialog.show();
                                et_id.setEnabled(false); // 아이디값 고정
                                validate = true;
                                btn_check.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이미 존재하는 아이디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                VaildateRequest validateRequest = new VaildateRequest(user_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        initMyAPI(BASE_URL);
        // 회원가입 버튼 클릭 시 수행
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginData data = new loginData();
                // 아이디
                data.setUser_id(et_id.getText().toString());
                // 비밀번호
                data.setPassword(et_pass.getText().toString());
                // 나이
                data.setAge(Integer.parseInt(et_age.getText().toString()));
                // 성별
                int radio_button_id = radioGroup.getCheckedRadioButtonId(); // 선택된 라디오버튼의 id값
                RadioButton rb = findViewById(radio_button_id);
                if(rb.getText().equals("남"))
                    user_sex = 0;
                else if(rb.getText().equals("여"))
                    user_sex = 1;
                data.setSex(user_sex);

                Call<loginData> registerCall = mMyAPI.post_sign(data);
                registerCall.enqueue(new Callback<loginData>() {
                    @Override
                    public void onResponse(Call<loginData> call, retrofit2.Response<loginData> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "회원가입 성공");
                            Toast.makeText(RegisterActivity.this,"회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else{
                            Log.d(TAG,"Status Code : " + response.code());
                            Log.d(TAG,response.errorBody().toString());
                            Log.d(TAG,call.request().body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<loginData> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
            }
        });

        // 취소 버튼 클릭시
        btn_cancle_regi = findViewById(R.id.btn_cancle_regi);
        btn_cancle_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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

    // 키보드 숨기기
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_id.getWindowToken(),0);
        imm.hideSoftInputFromWindow(et_pass.getWindowToken(),0);
        imm.hideSoftInputFromWindow(et_age.getWindowToken(),0);
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