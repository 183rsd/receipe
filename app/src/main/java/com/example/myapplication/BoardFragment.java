package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardFragment extends Fragment {
    TestItem post_dataList;
    List<allpostData> dataInfo;

    RecyclerView recyclerView;
    BoardRecyclerAdapter adapter;

    private String user_id; // user테이블 user_id
    int id; // user테이블 id
    FloatingActionButton reg_button;

    private final String TAG = BoardFragment.class.getSimpleName();
    // 서버 url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_board, container, false);

        initMyAPI(BASE_URL);

        reg_button = v.findViewById(R.id.fab_btn_boardFragment);

        Bundle bundle = getArguments(); // 메인액티비티2 에서 전달받은 번들 저장
        id = bundle.getInt("id",0);
        user_id = bundle.getString("user_id", "x"); // 사용자 아이디

        dataInfo = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recyclerview_board);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Call<TestItem> call = mMyAPI.get_allpost();
        call.enqueue(new Callback<TestItem>() {
            @Override
            public void onResponse(Call<TestItem> call, Response<TestItem> response) {
                if (response.isSuccessful()) {
                    post_dataList = response.body();
                    Log.d("BoardFragment", post_dataList.toString());

                    dataInfo = post_dataList.results;

                    adapter = new BoardRecyclerAdapter(getActivity(), dataInfo);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    Toast.makeText(getActivity(),"response.isNotSuccessful",Toast.LENGTH_LONG).show();
                    Log.d(TAG,"Status Code : " + response.code());
                    Log.d(TAG,response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<TestItem> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        // 글 작성하기 버튼 클릭 시
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity_board.class);
                intent.putExtra("id", id); // 사용자 id (int)
                intent.putExtra("user_id",user_id); // 사용자 id (string)
                startActivity(intent);
            }
        });



        return v;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI = retrofit.create(MyAPI.class);
    }

}