package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

public class RecipeActivity extends AppCompatActivity {
    int pic_user_id;
    Bitmap pic_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        pic_user_id = intent.getIntExtra("pic_user_id",0);

    }
}