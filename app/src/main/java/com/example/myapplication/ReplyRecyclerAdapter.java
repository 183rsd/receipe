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

public class ReplyRecyclerAdapter extends RecyclerView.Adapter<ReplyRecyclerAdapter.ReplyViewHolder>{

    private List<allReplyData> dataList_reply;

    private final Context mContext_reply;
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;
    private final String TAG = getClass().getSimpleName();

    public ReplyRecyclerAdapter(Context mContext_reply, List<allReplyData> dataList_reply){
        this.mContext_reply = mContext_reply;
        this.dataList_reply = dataList_reply;
    }

    @NonNull
    @Override
    public ReplyRecyclerAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        initMyAPI(BASE_URL);
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(mContext_reply).inflate(R.layout.item_recycler_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyRecyclerAdapter.ReplyViewHolder holder, int position) {

        holder.tv_userid.setText("" + dataList_reply.get(position).getUser_name());
        holder.tv_content.setText("" + dataList_reply.get(position).getReply_content());

    }



    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return dataList_reply.size();
    }


    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }


    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_userid, tv_content;
        public ReplyViewHolder(@NonNull View itemView){
            super(itemView);

            tv_userid=(TextView)itemView.findViewById(R.id.rp_userid_tv);
            tv_content=(TextView)itemView.findViewById(R.id.rp_content_tv);
        }
    }

}