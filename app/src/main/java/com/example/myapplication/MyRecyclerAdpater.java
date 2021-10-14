package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
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

class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private final ArrayList<ItemData> listData = new ArrayList<>();
    private final Context mContext;
    private String pic_list;
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;
    private final String TAG = getClass().getSimpleName();

    public MyRecyclerAdapter(Context mcontext){
        this.mContext = mcontext;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        initMyAPI(BASE_URL);
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));

    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(ItemData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
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
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.tv_menu);
            imageView = itemView.findViewById(R.id.iv_menu);


        }

        void onBind(ItemData data) {
            textView1.setText(data.getName());
            imageView.setImageResource(data.getImage());

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        switch (pos){
                            case 0 : pic_list = "갈비찜"; break;
                            case 1 : pic_list = "갈비탕"; break;
                            case 2 : pic_list = "갈치조림"; break;
                            case 3 : pic_list = "감자탕"; break;
                            case 4 : pic_list = "고등어조림"; break;
                            case 5 : pic_list = "김밥"; break;
                            case 6 : pic_list = "김치볶음밥"; break;
                            case 7 : pic_list = "김치찌개"; break;
                            case 8 : pic_list = "김치찜"; break;
                            case 9 : pic_list = "꽁치조림"; break;
                            case 10 : pic_list = "닭볶음탕"; break;
                            case 11 : pic_list = "도토리묵"; break;
                            case 12 : pic_list = "동태찌개"; break;
                            case 13 : pic_list = "된장찌개"; break;
                            case 14 : pic_list = "떡볶이"; break;
                            case 15 : pic_list = "만두국"; break;
                            case 16 : pic_list = "매운탕"; break;
                            case 17 : pic_list = "무국"; break;
                            case 18 : pic_list = "미역국"; break;
                            case 19 : pic_list = "북엇국"; break;
                            case 20 : pic_list = "비빔밥"; break;
                            case 21 : pic_list = "삼계탕"; break;
                            case 22 : pic_list = "새우볶음밥"; break;
                            case 23 : pic_list = "설렁탕"; break;
                            case 24 : pic_list = "소세지볶음"; break;
                            case 25 : pic_list = "수제비"; break;
                            case 26 : pic_list = "순두부찌개"; break;
                            case 27 : pic_list = "시래기국"; break;
                            case 28 : pic_list = "알밥"; break;
                            case 29 : pic_list = "열무국수"; break;
                            case 30 : pic_list = "육개장"; break;
                            case 31 : pic_list = "잔치국수"; break;
                            case 32 : pic_list = "잡채"; break;
                            case 33 : pic_list = "장조림"; break;
                            case 34 : pic_list = "제육볶음"; break;
                            case 35 : pic_list = "주꾸미볶음"; break;
                            case 36 : pic_list = "찜닭"; break;
                            case 37 : pic_list = "코다리조림"; break;
                            case 38 : pic_list = "콩국수"; break;
                            case 39 : pic_list = "파전"; break;
                            case 40 : pic_list = "해물찜"; break;

                        }
                        String s = pic_list.replaceAll(" ","");

                        Call<pictureData> getCall = mMyAPI.get_rec_name(s);
                        getCall.enqueue(new Callback<pictureData>() {
                            @Override
                            public void onResponse(Call<pictureData> call, Response<pictureData> response) {
                                if (response.isSuccessful()) {
                                    pictureData result = response.body();
                                    int rec_no = result.getRec_no();
                                    String rec_name = result.getRec_name();
                                    String rec_content = result.getRec_content();
                                    String rec_ingre = result.getRec_ingre();

                                    Intent intent1 = new Intent(mContext,RecipeActivity.class);
                                    intent1.putExtra("rec_no",rec_no);
                                    intent1.putExtra("rec_name",rec_name);
                                    intent1.putExtra("rec_content",rec_content);
                                    intent1.putExtra("rec_ingre",rec_ingre);
                                    mContext.startActivity(intent1);
                                }
                                else {
                                    Toast.makeText(mContext,"response.isNotSuccessful",Toast.LENGTH_LONG).show();
                                    Log.d(TAG,"Status Code : " + response.code());
                                    Log.d(TAG,response.errorBody().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<pictureData> call, Throwable t) {
                                Log.d(TAG,"Fail msg : " + t.getMessage());
                                Toast.makeText(mContext,"서버 오류",Toast.LENGTH_LONG).show();
                            }
                        });


                    }


                }
            });

        }

    }


}
