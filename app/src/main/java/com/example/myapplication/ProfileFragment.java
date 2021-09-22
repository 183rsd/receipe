package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    private TextView tv_id, tv_pw, tv_age, tv_sex;
    private int profile_age_int, profile_sex_int, id_code;
    private String profile_id, profile_pw, profile_age, profile_sex;
    private Button btn_update, btn_delete, btn_logout;
    Dialog update_dialog;

    private final String TAG = getClass().getSimpleName();
    // 서버 url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initMyAPI(BASE_URL);

        // 텍스트뷰
        tv_id = v.findViewById(R.id.profile_id);
        tv_pw = v.findViewById(R.id.profile_password);
        tv_age = v.findViewById(R.id.profile_age);
        tv_sex = v.findViewById(R.id.profile_sex);

        // 버튼
        btn_update = v.findViewById(R.id.btn_update);
        btn_delete = v.findViewById(R.id.btn_delete);
        btn_logout = v.findViewById(R.id.btn_logout);

        // 프로필 수정 버튼 다이얼로그
        update_dialog = new Dialog(getActivity());
        update_dialog.setContentView(R.layout.password_update_dialog);



        Bundle bundle = getArguments(); // 메인액티비티2 에서 전달받은 번들 저장
        id_code = bundle.getInt("id",0);
        profile_id = bundle.getString("user_id","x");
        profile_pw = bundle.getString("user_pw", "x");
        profile_age_int = bundle.getInt("user_age",1000);
        profile_age = Integer.toString(profile_age_int);

        profile_sex_int = bundle.getInt("user_sex",1000);
        if(profile_sex_int == 0)
            profile_sex = "남";
        else if (profile_sex_int == 1)
            profile_sex = "여";
        else
            profile_sex = "알수없음";

        tv_id.setText("아이디 : " + profile_id);
        tv_pw.setText("비밀번호 : " + profile_pw);
        tv_age.setText("나이 : " + profile_age);
        tv_sex.setText("성별 : " + profile_sex);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() { // 삭제 시 비밀번호 입력하게끔 만들어야함
            @Override
            public void onClick(View view) {
                profileDelete();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"로그인 화면으로 돌아갑니다.",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });



        //return inflater.inflate(R.layout.fragment_profile, container, false);
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

    public void profileUpdate(){ // 비밀번호변경 버튼 클릭시시
       update_dialog.show();
        EditText et_now_pw = update_dialog.findViewById(R.id.et_now_pw);
        EditText et_up_pw = update_dialog.findViewById(R.id.et_update_pw);
        Button update_yes = update_dialog.findViewById(R.id.update_yes);
        Button update_no = update_dialog.findViewById(R.id.update_no);

        update_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_now_pw.getText().toString().equals(profile_pw)){
                    loginData item = new loginData();
                    item.setPassword(et_up_pw.getText().toString());
                    Call<loginData> patchCall = mMyAPI.patch_pw(id_code,item.getPassword());
                    patchCall.enqueue(new Callback<loginData>() {
                        @Override
                        public void onResponse(Call<loginData> call, Response<loginData> response) {
                            if(response.isSuccessful()){
                                Log.d(TAG,"비밀번호 변경 완료 " + response.code());
                                Toast.makeText(getActivity(),"변경 완료. 변경된 비밀번호로 다시 로그인해주세요",Toast.LENGTH_LONG).show();
                                et_now_pw.setText(null);
                                et_up_pw.setText(null);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                Log.d(TAG,"Status Code : " + response.code());
                                et_now_pw.setText(null);
                                et_up_pw.setText(null);
                            }
                        }
                        @Override
                        public void onFailure(Call<loginData> call, Throwable t) {
                            Log.d(TAG,"Fail msg : " + t.getMessage());
                            et_now_pw.setText(null);
                            et_up_pw.setText(null);
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(),"현재 비밀번호가 올바르지 않습니다.",Toast.LENGTH_LONG).show();
                    et_now_pw.setText(null);
                    et_up_pw.setText(null);
                }


            }
        });

        update_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"취소",Toast.LENGTH_LONG).show();
                et_now_pw.setText(null);
                et_up_pw.setText(null);
                update_dialog.dismiss();
            }
        });
    }

    public void profileDelete(){

        final EditText et = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("계정 삭제");
        builder.setMessage("비밀번호를 입력하세요.");
        builder.setView(et);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(et.getText().toString().equals(profile_pw)){

                    Call<loginData> deleteCall = mMyAPI.delete_posts(id_code);
                    deleteCall.enqueue(new Callback<loginData>() {
                        @Override
                        public void onResponse(Call<loginData> call, Response<loginData> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "삭제 완료");
                                Toast.makeText(getActivity(), "삭제 완료. 로그인 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                                et.setText(null);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Log.d(TAG,"Status Code : " + response.code());
                                et.setText(null);
                            }
                        }
                        @Override
                        public void onFailure(Call<loginData> call, Throwable t) {
                            Log.d(TAG,"Fail msg : " + t.getMessage());
                            et.setText(null);
                        }
                    });

                }
                else{
                    Toast.makeText(getActivity(), "비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                    et.setText(null);
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"취소",Toast.LENGTH_LONG).show();
                et.setText(null);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
