package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.BoardViewHolder> {

    // adapter에 들어갈 list
    private List<allpostData> dataList;

    private final Context mContext;
    private String pic_list;
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;
    private final String TAG = getClass().getSimpleName();

    public BoardRecyclerAdapter(Context mContext, List<allpostData> dataList){
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public BoardRecyclerAdapter.BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        initMyAPI(BASE_URL);
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_board, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardRecyclerAdapter.BoardViewHolder holder, int position) {
        // Item을 하나하나 보여주는(bind 되는) 함수입니다.

        holder.tv_title.setText(""+dataList.get(position).getPost_title());
//        holder.tv_user_id.setText(""+dataList.get(position).getUser_id());
//        holder.tv_user_id.setText("111");


    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return dataList.size();
    }

    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }



    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    public class BoardViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        ImageView iv_board_img;
        TextView tv_user_id;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_board_title);
//            iv_board_img = itemView.findViewById(R.id.iv_board_img);
            tv_user_id = (TextView) itemView.findViewById(R.id.tv_board_userid);
        }
    }


}


