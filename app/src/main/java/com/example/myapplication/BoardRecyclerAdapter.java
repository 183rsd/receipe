//package com.example.myapplication;
//
//import android.annotation.SuppressLint;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.ContactsContract;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.view.menu.MenuView;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.BoardViewHolder> {
//
//    // adapter에 들어갈 list
//    private List<allpostData> dataList;
//
//    private final Context mContext;
//    private String pic_list;
//    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
//    private MyAPI mMyAPI;
//    private final String TAG = getClass().getSimpleName();
//
//    public BoardRecyclerAdapter(Context mContext, List<allpostData> dataList){
//        this.mContext = mContext;
//        this.dataList = dataList;
//    }
//
//    @NonNull
//    @Override
//    public BoardRecyclerAdapter.BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        initMyAPI(BASE_URL);
//        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
//        // return 인자는 ViewHolder 입니다.
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_board, parent, false);
//        return new BoardViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BoardRecyclerAdapter.BoardViewHolder holder, int position) {
//
////        holder.tv_post_no.setText(""+dataList.get(position).getId());
////        holder.tv_title.setText(""+dataList.get(position).getPost_title());
////        holder.tv_user_id.setText(""+dataList.get(position).getUser_name());
//        holder.onBind(dataList.get(position));
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() { // 리사이클러뷰 클릭하여 각 항목의 데이터 추출하는 방법.
//            @Override
//            public void onClick(View view) {
//                int id = Integer.parseInt(holder.tv_post_no.getText().toString()); // 각 항목의 tv_post_no 텍스트뷰에 적힌 값을 int로 받아옴.
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        // RecyclerView의 총 개수 입니다.
//        return dataList.size();
//    }
//
//    private void initMyAPI(String baseUrl){
//        Log.d(TAG,"initMyAPI : " + baseUrl);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        mMyAPI = retrofit.create(MyAPI.class);
//    }
//
//
//
//    // RecyclerView의 핵심인 ViewHolder 입니다.
//    // 여기서 subView를 setting 해줍니다.
//    public class BoardViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tv_title;
//        ImageView iv_board_img;
//        TextView tv_user_id;
//        TextView tv_post_no;
//
//        public BoardViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            tv_title = (TextView) itemView.findViewById(R.id.tv_board_title);
////            iv_board_img = itemView.findViewById(R.id.iv_board_img);
//            tv_user_id = (TextView) itemView.findViewById(R.id.tv_board_userid);
//            tv_post_no = (TextView) itemView.findViewById(R.id.tv_board_no);
//            itemView.setClickable(true);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getBindingAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION) {
//
//                    }
//                }
//            });
//        }
//
//
//        void onBind(allpostData data){
//            tv_post_no.setText(""+data.getId());
//            tv_title.setText(""+data.getPost_title());
//            tv_user_id.setText(""+data.getUser_name());
//        }
//
//    }
//
//
//}

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
    private final List<allpostData> dataList;

    private final Context mContext;
    private String pic_list;
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;
    private final String TAG = getClass().getSimpleName();
    private int id;


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

        holder.onBind(dataList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() { // 리사이클러뷰 클릭하여 각 항목의 데이터 추출하는 방법.
            @Override
            public void onClick(View view) {
                id = Integer.parseInt(holder.tv_post_no.getText().toString()); // 각 항목의 tv_post_no 텍스트뷰에 적힌 값을 int로 받아옴.

                Call<allpostData> getCall = mMyAPI.get_board_list(id);
                getCall.enqueue(new Callback<allpostData>() {
                    @Override
                    public void onResponse(Call<allpostData> call, Response<allpostData> response) {
                        if (response.isSuccessful()) {
                            allpostData result = response.body();
                            int post_no = result.getId();
                            String user_name = result.getUser_name();
                            String post_title = result.getPost_title();
                            String post_content = result.getPost_content();
                            String post_img_string= result.getImg_string();

                            Intent intent3 = new Intent(mContext, BoardActivity.class);
                            intent3.putExtra("id", post_no);
                            intent3.putExtra("user_name", user_name);
                            intent3.putExtra("post_title", post_title);
                            intent3.putExtra("post_content", post_content);
                            intent3.putExtra("post_img_string", post_img_string);
                            mContext.startActivity(intent3);
                        } else {
                            Toast.makeText(mContext, "response.isNotSuccessful", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Status Code : " + response.code());
                            Log.d(TAG, response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<allpostData> call, Throwable t) {

                    }
                });
            }
        });

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

        private TextView tv_title;
        private ImageView iv_board_img;
        private TextView tv_user_id;
        private TextView tv_post_no;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_board_title);
//            iv_board_img = itemView.findViewById(R.id.iv_board_img);
            tv_user_id = (TextView) itemView.findViewById(R.id.tv_board_userid);
            tv_post_no = (TextView) itemView.findViewById(R.id.tv_board_no);
        }
        void onBind(allpostData data) {
            tv_post_no.setText(""+data.getId());
            tv_title.setText(""+data.getPost_title());
            tv_user_id.setText(""+data.getUser_name());

            itemView.setClickable(true);

        }
    }



}


