package com.example.myapplication;
        import androidx.annotation.Nullable;
        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentTransaction;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Rect;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Toast;
        import android.view.LayoutInflater;;
        import android.view.ViewGroup;

        import com.bumptech.glide.Glide;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.HashMap;

        import okhttp3.MediaType;
        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;
public class RegisterActivity_board extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    // ?????? url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;
    private Context mContext;

    // ????????? ???????????? ??????
    EditText title_et, content_et;
    Button reg_button, btn_register_board;
    ImageView imageView;
    private AlertDialog dialog;

    // ??????????????? ??????
    int user_id_int;
    String user_id_str = "";

    // ??????????????? ?????? ??????
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    String filePath;
    MultipartBody.Part uploadFile;
    File send_file;
    boolean pic_regist_success = false;
    Uri photoUri;

    // retrofit2?????? ????????? ?????????
    HashMap<String, RequestBody> map;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_board);

        // Boardfragment ?????? ?????? user_id ??? ????????? ??????
        Intent intent = getIntent();
        user_id_int = intent.getIntExtra("id",0);
        user_id_str = intent.getStringExtra("user_id");

        // ???????????? ?????????
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);
//        btn_register_board = findViewById(R.id.btn_register_board);
        imageView = findViewById(R.id.iv_register_board);
        initMyAPI(BASE_URL);

        // ?????? ?????? ??????
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        // ?????? ????????? ??????
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pic_regist_success){ // ?????? ?????? ????????? ????????? ?????? ??????
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity_board.this);
                    dialog = builder.setMessage("????????? ??????????????????.").setNegativeButton("??????", null).create();
                    dialog.show();
                    return;
                }
                else{
                    // ????????? ?????? ??????
                    String content = content_et.getText().toString();
                    String title = title_et.getText().toString();

                    RequestBody post_user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id_int));
                    RequestBody post_content = RequestBody.create(MediaType.parse("text/plain"),content);
                    RequestBody post_title = RequestBody.create(MediaType.parse("text/plain"),title);
                    map = new HashMap<>();
                    map.put("user_id",post_user_id);
                    map.put("post_content",post_content);
                    map.put("post_title",post_title);

                    filePath = getRealPathFromURI(photoUri);

                    send_file = new File(filePath);
                    InputStream inputStream = null;
                    try{
                        inputStream = getContentResolver().openInputStream(photoUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());
                    uploadFile = MultipartBody.Part.createFormData("post_img",send_file.getName(),requestBody); // name(key)?????? ???????????? ???????????? ????????? ???????????? ?????????


                    if(uploadFile!=null){
                        Call<postcreateData> postContent = mMyAPI.post_list(map, uploadFile);
                        postContent.enqueue(new Callback<postcreateData>() {
                            @Override
                            public void onResponse(Call<postcreateData> call, Response<postcreateData> response) {
                                if(response.isSuccessful()){

                                    Log.d(TAG, "????????? ????????????");
                                    Toast.makeText(RegisterActivity_board.this, "???????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                                    finish();


//                                    BoardRecyclerAdapter adapter = null;
//                                    adapter.notifyDataSetChanged();

//                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                    Fragment fragment1 = new BoardFragment();
//                                    ft.replace(R.id.)
//
//                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                                    // ??????????????????????????? ?????? ??????
//                                    Fragment fragment1= new Fragment(); // ?????? ??????
//                                    transaction.replace(R.id.fragment_main, fragment1); //layout, ????????? layout
//                                    transaction.commit(); //commit?????? ?????? ?????? ????????? ?????? ????????? ?????? ??????
                                }
                                else{
                                    Toast.makeText(RegisterActivity_board.this,response.message(),Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<postcreateData> call, Throwable t) {
                                Log.d("fail msg : ",t.getMessage());
                                Toast.makeText(RegisterActivity_board.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity_board.this, "??????????????????.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, "?????? ???????????????.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e("TAG", tempFile.getAbsolutePath() + " ?????? ??????");
                        tempFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {

            try{ // ??????????????? ????????? ??????(??????x) uri??? UriToPath????????? ?????? filePath ?????? (????????????)
                photoUri = data.getData();    // ?????? ??? ?????? ?????? ?????? ??? file:// ??? ?????? content:// uri??? ???????????? ???. (?????? ??????)
                Glide.with(this).load(photoUri).into(imageView);
//                uri_text.setText(photoUri.toString()); // uri??? content://??? ?????????. ?????? file://??? ????????? filePath????
                // https://black-jin0427.tistory.com/120

                pic_regist_success = true;  // ????????????????????? true??? ??????


            }catch (Exception e){

            }
        }
    }



    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI = retrofit.create(MyAPI.class);
    }

    // uri??? ?????? ???????????? ??????
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    // ?????? ?????? ??? ????????? ?????????
    public boolean dispatchTouchEvent(MotionEvent ev){
        View focusView = getCurrentFocus();
        if(focusView != null){
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if(!rect.contains(x,y)){
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


}

