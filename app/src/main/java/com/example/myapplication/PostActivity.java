package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    // private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private EditText mTitle, mContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTitle = findViewById(R.id.post_title_edit);
        mContents = findViewById(R.id.post_contents_edit);

        findViewById(R.id.post_save_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if (mAuth.getCurrentUser() != null){
//            String postId = mStore.collection(FirebaseID.post).document().getId();
//            Map<String, Object> data = new HashMap<>();
//            data.put(FirebaseID.documentId, mAuth.getCurrentUser.getUid());
//            data.put(FirebaseId.title, mTitle.getText().toString());
//            data.put(FirebaseId.contents, mContents.getText().toString());
//            mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
//            finish();
//        } 1:44:37에 파이어베이스 클래스
    }
}