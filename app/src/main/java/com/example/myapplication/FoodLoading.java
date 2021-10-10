package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class FoodLoading extends Dialog {
    public FoodLoading(Context context){
        super(context);
        // 다이얼로그 제목 안보이게
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.food_loading);

    }
}
