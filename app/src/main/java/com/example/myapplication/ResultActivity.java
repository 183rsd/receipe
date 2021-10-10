package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = ResultActivity.class.getSimpleName();
    // 서버 url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    private MyAPI mMyAPI;
    private Context mContext;


    Uri uri;
    int pic_user_id;
    String pic_user_id_str;
    Context ctx = this;
    String filePath, img_absolute_path, pic_img_file_name;
    LottieAnimationView animationView;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        byte[] byteArray = intent.getByteArrayExtra("image");
        pic_user_id = intent.getIntExtra("pic_user_id",0);
        pic_user_id_str = Integer.toString(pic_user_id);

        Bitmap bitmap_pic = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        initMyAPI(BASE_URL);





        animationView = findViewById(R.id.result_loading);
        animationView.setAnimation("loading_2.json");
        animationView.loop(true);
        animationView.playAnimation();



        ImageView get_img_iv = findViewById(R.id.get_img_iv);
//        TextView get_user_id = findViewById(R.id.get_user_id);
        TextView filePath_tv = findViewById(R.id.filePath);
        Button btn_pic = findViewById(R.id.pic_btn);
        TextView realPath = findViewById(R.id.realPath);



        uri = getImageUri(ctx,bitmap_pic);
//        filePath = UriToPath(ctx,uri); // 상대경로
//        img_absolute_path = createCopyAndReturnRealPath(ctx,uri); // 절대경로

        ImageView imageView = findViewById(R.id.result_Image);
        Glide.with(this).load(uri).into(imageView);




//        get_user_id.setText("\nuser_id : " + pic_user_id_str);
//        filePath_tv.setText("\n상대경로 : " + filePath);
//        realPath.setText("\n절대경로 : " + img_absolute_path);


        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(pic_user_id));
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("user_id",user_id);





        btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File send_file = new File(filePath);
                InputStream inputStream = null;
                try{
                    inputStream = ctx.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());
                MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("pic_img",send_file.getName(),requestBody);




                Call<pictureData> pic_call = mMyAPI.post_picture(map, uploadFile);
                pic_call.enqueue(new Callback<pictureData>() {
                    @Override
                    public void onResponse(Call<pictureData> call, Response<pictureData> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(ResultActivity.this,"post 성공",Toast.LENGTH_LONG).show();
                            Log.d(TAG,"Status Code : " + response.code());
                        }
                        else{
                            Toast.makeText(ResultActivity.this,"post 실패",Toast.LENGTH_LONG).show();
                            Log.d(TAG,"Status Code : " + response.code());
                            Log.d(TAG,response.errorBody().toString());
                            Log.d(TAG,call.request().body().toString());

                        }
                    }

                    @Override
                    public void onFailure(Call<pictureData> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                        Toast.makeText(ResultActivity.this,"서버 오류",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });





    }
    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }



    private Uri getImageUri(Context ctx, Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(),bitmap,"Title",null);
        return Uri.parse(path);
    }

    public static String UriToPath(Context ctx,Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = ctx.getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri1 = Uri.fromFile(new File(path));

        cursor.close();
        return path;
    }




    private void BitmapToFile(Bitmap bitmap, String strFilePath){
        File file = new File(strFilePath);

        OutputStream out = null; // bitmap데이터를 outputStream에 받아 file에 넣어주는 용도
        try{
            file.createNewFile(); // 파일 초기화
            out = new FileOutputStream(file); // outputStream에 file 넣어줌
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                out.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String saveBitmapToJpg(Bitmap bitmap, String name){
        File storage = getCacheDir();
        String fileName = name+".jpg";
        File imgFile = new File(storage,fileName);
        try {
            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("saveBitmapToJpg","FileNotFoundException : "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("imgPath",getCacheDir()+"/"+fileName);
        return getCacheDir()+"/"+fileName;
    }

    public static String createCopyAndReturnRealPath(Context ctx, Uri uri){
        final ContentResolver contentResolver = ctx.getContentResolver();

        if(contentResolver == null)
            return null;

        String filePath = ctx.getApplicationInfo().dataDir + File.separator + System.currentTimeMillis(); // 파일경로

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if(inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = inputStream.read(buf)) > 0){
                outputStream.write(buf,0,len);
                outputStream.close();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateImage(Uri uri, Bitmap bitmap) throws IOException{
        InputStream in = getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(in);
        in.close();

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();
        if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
            matrix.postRotate(90);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180){
            matrix.postRotate(180);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270){
            matrix.postRotate(270);
        }
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }







}