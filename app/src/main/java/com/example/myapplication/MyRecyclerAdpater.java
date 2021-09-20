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

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private final ArrayList<ItemData> listData = new ArrayList<>();
    private final Context mContext;

    public MyRecyclerAdapter(Context mcontext){
        this.mContext = mcontext;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
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

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView1;
        private final ImageView imageView;




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

                    Intent intent0 = new Intent(mContext,List_result_Activity.class); // 귀칼
                    mContext.startActivity(intent0);
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION){
//                        switch (pos){
//                            case 0 : Intent intent0 = new Intent(mContext,List_result_Activity.class); // 귀칼
//                                mContext.startActivity(intent0);
//                                break;
//
//                        }
//                    }
                }
            });
        }

    }


}
