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
    // 서버 url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;
    private Context mContext;

    // 사용할 컴포넌트 선언
    EditText title_et, content_et;
    Button reg_button, btn_register_board;
    ImageView imageView;
    private AlertDialog dialog;

    // 유저아이디 변수
    int user_id_int;
    String user_id_str = "";

    // 갤러리에서 사진 등록
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    String filePath;
    MultipartBody.Part uploadFile;
    File send_file;
    boolean pic_regist_success = false;
    Uri photoUri;

    // retrofit2으로 전송할 해시맵
    HashMap<String, RequestBody> map;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_board);

        // Boardfragment 에서 넘긴 user_id 를 변수로 받음
        Intent intent = getIntent();
        user_id_int = intent.getIntExtra("id",0);
        user_id_str = intent.getStringExtra("user_id");

        // 컴포넌트 초기화
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);
//        btn_register_board = findViewById(R.id.btn_register_board);
        imageView = findViewById(R.id.iv_register_board);
        initMyAPI(BASE_URL);

        // 사진 등록 버튼
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        // 버튼 이벤트 추가
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pic_regist_success){ // 사진 등록 안하면 게시글 등록 불가
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity_board.this);
                    dialog = builder.setMessage("사진을 등록해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                else{
                    // 게시물 등록 함수
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
                    uploadFile = MultipartBody.Part.createFormData("post_img",send_file.getName(),requestBody); // name(key)값은 서버에서 설정해준 키값과 동일하게 해야함


                    if(uploadFile!=null){
                        Call<postcreateData> postContent = mMyAPI.post_list(map, uploadFile);
                        postContent.enqueue(new Callback<postcreateData>() {
                            @Override
                            public void onResponse(Call<postcreateData> call, Response<postcreateData> response) {
                                if(response.isSuccessful()){

                                    Log.d(TAG, "게시글 등록성공");
                                    Toast.makeText(RegisterActivity_board.this, "게시글이 등록되었습니다.",Toast.LENGTH_SHORT).show();
                                    finish();


//                                    BoardRecyclerAdapter adapter = null;
//                                    adapter.notifyDataSetChanged();

//                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                    Fragment fragment1 = new BoardFragment();
//                                    ft.replace(R.id.)
//
//                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                                    // 프래그먼트매니저를 통해 사용
//                                    Fragment fragment1= new Fragment(); // 객체 생성
//                                    transaction.replace(R.id.fragment_main, fragment1); //layout, 교체될 layout
//                                    transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
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
                        Toast.makeText(RegisterActivity_board.this, "사진등록안됨.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e("TAG", tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {

            try{ // 갤러리에서 가져온 사진(크롭x) uri는 UriToPath함수를 통해 filePath 추출 (상대경로)
                photoUri = data.getData();    // 다른 앱 간에 파일 공유 시 file:// 가 아닌 content:// uri를 사용해야 함. (보안 강화)
                Glide.with(this).load(photoUri).into(imageView);
//                uri_text.setText(photoUri.toString()); // uri가 content://로 시작함. 이걸 file://로 바꿔서 filePath로?
                // https://black-jin0427.tistory.com/120

                pic_regist_success = true;  // 사진등록체크를 true로 변경


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

    // uri를 통해 절대경로 추출
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    // 화면 터치 시 키보드 내려감
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

