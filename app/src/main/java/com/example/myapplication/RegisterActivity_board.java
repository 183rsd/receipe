package com.example.myapplication;
        import androidx.annotation.Nullable;
        import androidx.annotation.RequiresApi;
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
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.View;
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
    // 유저아이디 변수
    String user_id = "";
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    String filePath;
    MultipartBody.Part uploadFile;
    File send_file;
    HashMap<String, RequestBody> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_board);
        // Boardfragment 에서 넘긴 userid 를 변수로 받음
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        // 컴포넌트 초기화
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);
        btn_register_board = findViewById(R.id.btn_register_board);
        imageView = findViewById(R.id.iv_register_board);
        initMyAPI(BASE_URL);

        // 사진 등록 버튼
        btn_register_board.setOnClickListener(new View.OnClickListener() {
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


//              게시물 등록 함수
                String content = content_et.getText().toString();
                String title = title_et.getText().toString();

                RequestBody post_user_id = RequestBody.create(MediaType.parse("text/plain"),user_id);
                RequestBody post_content = RequestBody.create(MediaType.parse("text/plain"),content);
                RequestBody post_title = RequestBody.create(MediaType.parse("text/plain"),title);
                map = new HashMap<>();
                map.put("user_id",post_user_id);
                map.put("post_content",post_content);
                map.put("post_title",post_title);


                Call<contentData> postContent = mMyAPI.post_list(map,uploadFile);
                postContent.enqueue(new Callback<contentData>() {
                    @Override
                    public void onResponse(Call<contentData> call, retrofit2.Response<contentData> response) {
                        if(response.isSuccessful()){

                            Log.d(TAG, "게시글 등록성공");
                            Toast.makeText(RegisterActivity_board.this, "게시글이 등록되었습니다.",Toast.LENGTH_SHORT).show();


                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            // 프래그먼트매니저를 통해 사용
                            Fragment fragment1= new Fragment(); // 객체 생성
                            transaction.replace(R.id.fragment_main, fragment1); //layout, 교체될 layout
                            transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
                        }
                        else{
                            Toast.makeText(RegisterActivity_board.this,"notSuccessful",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<contentData> call, Throwable t) {
                        Toast.makeText(RegisterActivity_board.this,"onFailure",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, "취소 되었습니다."+resultCode, Toast.LENGTH_SHORT).show();

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
                Uri photoUri = data.getData();    // 다른 앱 간에 파일 공유 시 file:// 가 아닌 content:// uri를 사용해야 함. (보안 강화)
                Glide.with(this).load(photoUri).into(imageView);
//                uri_text.setText(photoUri.toString()); // uri가 content://로 시작함. 이걸 file://로 바꿔서 filePath로?
                // https://black-jin0427.tistory.com/120

                filePath = UriToPath(mContext,photoUri);

                send_file = new File(filePath);
                InputStream inputStream = null;
                try{
                    inputStream = mContext.getContentResolver().openInputStream(photoUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());
                uploadFile = MultipartBody.Part.createFormData("pic_img",send_file.getName(),requestBody);

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
    // 사진 uri 값을 통해 상대경로 추출
    public static String UriToPath(Context mContext,Uri photoUri){
        Cursor cursor = null;
        String[] proj = {MediaStore.Images.Media.DATA};

        assert photoUri != null;
        cursor = mContext.getContentResolver().query(photoUri, proj, null, null, null);

        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        @SuppressLint("Range") String path = cursor.getString(column_index);
        Uri uri1 = Uri.fromFile(new File(path));

        cursor.close();
        return path;
    }
}

