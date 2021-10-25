package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardActivity extends AppCompatActivity {
    private final String TAG = BoardActivity.class.getSimpleName();
    // 서버 url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;

    TextView tv_title, tv_content, tv_id;
    int rec_no;
    EditText et_reply;
    Button post_reply_bt;
    ImageView post_img;
    RecyclerView recyclerView;

    String user_name, rec_title, rec_content, rec_img;
    Bitmap pic_img;

    RecyclerView recyclerView_reply;
    ReplyRecyclerAdapter adapter;
    TestItem_reply reply_dataList;
    List<allReplyData> dataInfo_reply;

    // retrofit2으로 전송할 해시맵
    HashMap<String, RequestBody> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        initMyAPI(BASE_URL);

        Intent intent = getIntent();
        rec_no = intent.getIntExtra("id", 1);
        user_name = intent.getStringExtra("user_name");
        rec_title = intent.getStringExtra("post_title");
        rec_content = intent.getStringExtra("post_content");
        rec_img = intent.getStringExtra("post_img_string");

        tv_title = findViewById(R.id.title_tv);
        tv_id = findViewById(R.id.userid_tv);
        tv_content = findViewById(R.id.content_tv);
        post_reply_bt = findViewById(R.id.post_reply_button);
        post_img = findViewById(R.id.content_pic_tv);

        et_reply=findViewById(R.id.comment_et);

        tv_title.setText(rec_title);
        tv_id.setText(user_name);
        tv_content.setText(rec_content);

        pic_img = StringToBitmap(rec_img);
        post_img.setImageBitmap(pic_img);
//        Glide.with(this).load(rec_img).into(post_img);


        recyclerView_reply = findViewById(R.id.recyclerview_reply);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_reply.setLayoutManager(layoutManager);


        get_allreply(); //댓글통신함수 호출


        post_reply_bt.setOnClickListener(new View.OnClickListener() { // 댓글 작성 버튼 클릭
            @Override
            public void onClick(View view) {
                if(et_reply.length()==0){
                    Toast.makeText(BoardActivity.this,"1글자 이상 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    String content=et_reply.getText().toString();

                    postReply postReply = new postReply(rec_no, 3, content);
                    Call<postReply> post_reply = mMyAPI.post_reply(rec_no, postReply);
                    post_reply.enqueue(new Callback<postReply>() {
                        @Override
                        public void onResponse(Call<postReply> call, Response<postReply> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(BoardActivity.this, "댓글을 입력하였습니다", Toast.LENGTH_SHORT).show();
                                get_allreply();
                            }
                            else{
                                Toast.makeText(BoardActivity.this, "응답실패 : "+response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<postReply> call, Throwable t) {
                            Toast.makeText(BoardActivity.this, "onFailure : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }




            }
        });

    }


    public void get_allreply(){
        dataInfo_reply = new ArrayList<>();
        Call<TestItem_reply> call = mMyAPI.get_allReply(rec_no);
        call.enqueue(new Callback<TestItem_reply>() {
            @Override
            public void onResponse(Call<TestItem_reply> call, Response<TestItem_reply> response) {
                if (response.isSuccessful()) {
                    reply_dataList = response.body();
                    Log.d("BoardActivity", reply_dataList.toString());

                    dataInfo_reply = reply_dataList.results;

                    adapter = new ReplyRecyclerAdapter(BoardActivity.this, dataInfo_reply);
                    recyclerView_reply.setAdapter(adapter);

                } else {
                    Toast.makeText(BoardActivity.this, "response.isNotSuccessful", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Status Code : " + response.code());
                    Log.d(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<TestItem_reply> call, Throwable t) {
                Log.d(TAG, "Fail msg : " + t.getMessage());
                Toast.makeText(BoardActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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

    /* * String형을 BitMap으로 변환시켜주는 함수 * */
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
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