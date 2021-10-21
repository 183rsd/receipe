package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

public class BoardActivity extends AppCompatActivity {
    TextView tv_rec_no, tv_rec_name, tv_rec_ingre, tv_rec_content;
    int rec_no;
    String rec_name, rec_ingre, rec_content;
    Bitmap pic_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
//        rec_no = intent.getIntExtra("rec_no",0);
//        rec_name = intent.getStringExtra("rec_name");
//        rec_ingre = intent.getStringExtra("rec_ingre");
//        rec_content = intent.getStringExtra("rec_content");
//
////        tv_rec_no = findViewById(R.id.tv_rec_no);
//        tv_rec_name = findViewById(R.id.tv_rec_name);
//        tv_rec_ingre = findViewById(R.id.tv_rec_ingre);
//        tv_rec_content = findViewById(R.id.tv_rec_content);
//
////        tv_rec_no.setText("레시피번호 : "+ String.valueOf(rec_no));
//        tv_rec_name.setText(rec_name);
//        tv_rec_ingre.setText("필요 재료 : "+rec_ingre);
//        tv_rec_content.setText(rec_content);







    }
}