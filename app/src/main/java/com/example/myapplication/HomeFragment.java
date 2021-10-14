package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    Button btnCamera;
    Button btnGallery;
//    Button btn_go_recipe;
    ImageView imageView;
    File file;
    int pic_user_id;
    Uri photoUri, send_uri;
    String filePath;
    Context ctx = getActivity();
    Activity activity;
    FoodLoading customProgressDialog;
    private Animation anim;


    // db연동
    private static final String TAG = HomeFragment.class.getSimpleName();
    // 서버 url
    private final String BASE_URL = "https://restserver-lzssy.run.goorm.io";
    // 레시피 서버 주소는 계속 변경됨. 그때그때 수정(goorm.io에는 ai모델을 올릴 수가 없어서)
    private final String recipe_url = "https://7a6d-210-178-44-55.ngrok.io";
    private MyAPI mMyAPI, mMyAPI_recipe;
    private Context mContext;


    private final Boolean isPermisson = true;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;

    private Boolean isCamera = false; // 이미지를 리사이징하는 단계에서 카메라에서 온 화면인지, 앨범에서 온 화면인지 구분분

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        tedPermission();
       initMyAPI(BASE_URL);
       initMyAPI_recipe(recipe_url);


//        File sdcard = Environment.getExternalStorageDirectory();
//        file = new File(sdcard, "capture.jpg");

        View v = inflater.inflate(R.layout.fragment_home, container, false);


       // 로딩창 객체 생성
       customProgressDialog = new FoodLoading(getActivity());
       // 로딩창 투명하게 설정
       customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       imageView = v.findViewById(R.id.img_select_photo);
       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               photoDialogRadio();
           }
       });



        Bundle bundle_pic = getArguments(); // 메인액티비티2 에서 전달받은 번들 저장

        pic_user_id = bundle_pic.getInt("id",0);



        return v;
//        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    // 카메라 캡쳐 함수
    private void capture() { // takePhoto

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "이미지 처리 오류. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//            getActivity().finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                // 카메라 함수에서 photoUri는 file://형태의 uri 이 아니라, content://형태의 uri 임.
                // 다른 앱 간에 파일 공유 시 file:// 가 아닌 content:// uri를 사용해야 함. (보안 강화)
                photoUri = FileProvider.getUriForFile(getActivity(),"com.example.myapplication.provider",tempFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            } else{
                photoUri = Uri.fromFile(tempFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException { // 카메라로 촬영한 사진을 저장
       // 이렇게 안하면 이미지뷰에 사진 썸네일만 띄워서 사진이 흐릿하고 작음.

        // 이미지 파일 이름 ( img_{시간}_)
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp+ ".jpg";

        // 이미지가 저장될 폴더 이름 (/)
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/");
        if (!storageDir.exists())
            storageDir.mkdirs();

        // 빈 파일 생성
//        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        File image = new File(storageDir, imageFileName);

        return image;
    }

    // 갤러리 사진 로드 함수
    public void gallery() { // goToAlbum
        isCamera = true;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);



    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(getActivity(), "취소 되었습니다."+resultCode, Toast.LENGTH_SHORT).show();

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
                Glide.with(getActivity()).load(photoUri).into(imageView);
//                uri_text.setText(photoUri.toString()); // uri가 content://로 시작함. 이걸 file://로 바꿔서 filePath로?
                                                        // https://black-jin0427.tistory.com/120
                customProgressDialog.show();
                filePath = UriToPath(mContext,photoUri);

                RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(pic_user_id));
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("user_id",user_id);


                File send_file = new File(filePath);
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
                MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("pic_img",send_file.getName(),requestBody);


                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<pictureData> pic_call = mMyAPI_recipe.post_picture(map, uploadFile);
                        pic_call.enqueue(new Callback<pictureData>() {
                            @Override
                            public void onResponse(Call<pictureData> call, Response<pictureData> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(getActivity(),"post 성공",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG,"Status Code : " + response.code());
                                    customProgressDialog.dismiss();
                                    pictureData result = response.body();
                                    int rec_no = result.getRec_no();
                                    String rec_name = result.getRec_name();
                                    String rec_content = result.getRec_content();
                                    String rec_ingre = result.getRec_ingre();

                                    Intent intent1 = new Intent(getActivity(),RecipeActivity.class);
                                    intent1.putExtra("rec_no",rec_no);
                                    intent1.putExtra("rec_name",rec_name);
                                    intent1.putExtra("rec_content",rec_content);
                                    intent1.putExtra("rec_ingre",rec_ingre);
                                    startActivity(intent1);

                                }
                                else{
                                    Toast.makeText(getActivity(),"post 실패" ,Toast.LENGTH_SHORT).show();
                                    customProgressDialog.dismiss();
                                    Log.d(TAG,"Status Code : " + response.code());
                                    Log.d(TAG,response.errorBody().toString());
                                    Log.d(TAG,call.request().body().toString());

                                }
                            }

                            @Override
                            public void onFailure(Call<pictureData> call, Throwable t) {
                                Log.d(TAG,"Fail msg : " + t.getMessage());
                                Toast.makeText(getActivity(),"서버 오류",Toast.LENGTH_SHORT).show();
                                customProgressDialog.dismiss();
                            }
                        });
                    }
                }, 2500);


            }catch (Exception e){

            }

        } else if (requestCode == PICK_FROM_CAMERA){
            // 카메라를 통해 가져온 사진(크롭된사진) uri는 getRealPathFromUri함수를 통해 절대경로 추출.
            // 이유 : 카메라에서
            Glide.with(getActivity()).load(photoUri).into(imageView);
            customProgressDialog.show();
            filePath = getRealPathFromURI(photoUri);

            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(pic_user_id));
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("user_id",user_id);


            File send_file = new File(filePath);
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
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("pic_img",send_file.getName(),requestBody);


            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Call<pictureData> pic_call = mMyAPI_recipe.post_picture(map, uploadFile);
                    pic_call.enqueue(new Callback<pictureData>() {
                        @Override
                        public void onResponse(Call<pictureData> call, Response<pictureData> response) {
                            if(response.isSuccessful()){
                                customProgressDialog.dismiss();
                                pictureData result = response.body();
                                int rec_no = result.getRec_no();
                                String rec_name = result.getRec_name();
                                String rec_content = result.getRec_content();
                                String rec_ingre = result.getRec_ingre();

                                Intent intent1 = new Intent(getActivity(),RecipeActivity.class);
                                intent1.putExtra("rec_no",rec_no);
                                intent1.putExtra("rec_name",rec_name);
                                intent1.putExtra("rec_content",rec_content);
                                intent1.putExtra("rec_ingre",rec_ingre);
                                startActivity(intent1);


                                Toast.makeText(getActivity(),"post 성공",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"Status Code : " + response.code());
                            }
                            else{
                                Toast.makeText(getActivity(),"post 실패",Toast.LENGTH_SHORT).show();
                                customProgressDialog.dismiss();
                                Log.d(TAG,"Status Code : " + response.code());
                                Log.d(TAG,response.errorBody().toString());
                                Log.d(TAG,call.request().body().toString());

                            }
                        }

                        @Override
                        public void onFailure(Call<pictureData> call, Throwable t) {
                            Log.d(TAG,"Fail msg : " + t.getMessage());
                            Toast.makeText(getActivity(),"서버 오류",Toast.LENGTH_SHORT).show();
                            customProgressDialog.dismiss();
                        }
                    });
                }
            },2500);


        }
    }



    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

//    private void recentImage() {
//
////        ImageResizeUtils.resizeFile(tempFile,tempFile,1280,isCamera);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
////            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        // 비트맵 크기 줄이기
//        float scale = 1024 / (float) originalBm.getWidth();
//        int image_w = (int) (originalBm.getWidth() * scale);
//        int image_h = (int) (originalBm.getHeight() * scale);
//
//        Bitmap resize = Bitmap.createScaledBitmap(originalBm, image_w, image_h, true);
//        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//
//        Intent intent = new Intent(getActivity(), ResultActivity.class);
//        intent.putExtra("image", byteArray);
//        intent.putExtra("pic_user_id",pic_user_id);
//        startActivity(intent);
//    }

    private void photoDialogRadio(){
        final CharSequence[] PhotoModels = {"카메라로 촬영", "갤러리에서 선택"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());

        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("사진 선택");
        alt_bld.setItems(R.array.select_photo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Toast.makeText(ProfileActivity.this, PhotoModels[item] + "가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                if (item == 0) { // 카메라
                    capture();
                    alt_bld.create().dismiss();


                } else if (item == 1) { // 갤러리
                    gallery();
                    alt_bld.create().dismiss();
                }
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

    private void initMyAPI_recipe(String recipe_url){
        Log.d(TAG,"initMyAPI : " + recipe_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(recipe_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI_recipe = retrofit.create(MyAPI.class);
    }



    // 비트맵에서 uri 추출
//    private Uri getImageUri(Context ctx, Bitmap bitmap){
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(),bitmap,"Title",null);
//        return Uri.parse(path);
//    }


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

    // uri로 절대경로
//    public static String createCopyAndReturnRealPath(Context mContext, Uri uri){
//        final ContentResolver contentResolver = mContext.getContentResolver();
//
//        if(contentResolver == null)
//            return null;
//
//        String filePath = mContext.getApplicationInfo().dataDir + File.separator + System.currentTimeMillis(); // 파일경로
//
//        File file = new File(filePath);
//        try {
//            InputStream inputStream = contentResolver.openInputStream(uri);
//            if(inputStream == null)
//                return null;
//
//            OutputStream outputStream = new FileOutputStream(file);
//            byte[] buf = new byte[1024];
//            int len;
//            while((len = inputStream.read(buf)) > 0){
//                outputStream.write(buf,0,len);
//                outputStream.close();
//                inputStream.close();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file.getAbsolutePath();
//    }

    @Override
    public void onAttach(Context context) { // Fragment를 Activity에 attach할 때 호출.
       // Fragment 생명주기의 첫 단계
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity)
            activity = (Activity) context;
    }

//    @SuppressLint("Range")
//    public static Uri convertContentToFileUri(Context mContext, Uri uri) throws Exception {
//        Cursor cursor = null;
//        try {
//            cursor = mContext.getContentResolver().query(uri, null, null, null, null);
//            cursor.moveToNext();
//            return Uri.fromFile(new File(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))));
//        } finally {
//            if(cursor != null)
//                cursor.close();
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPathFromURI(Uri contentUri) { // content uri를 절대경로로 변환
       if (contentUri.getPath().startsWith("/storage")) {
           return contentUri.getPath();
       }
       String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
       String[] columns = { MediaStore.Files.FileColumns.DATA };
       String selection = MediaStore.Files.FileColumns._ID + " = " + id;
       Cursor cursor = mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
       try { int columnIndex = cursor.getColumnIndex(columns[0]);
           if (cursor.moveToFirst()) {
               return cursor.getString(columnIndex); }
       } finally {
           cursor.close();
       }
       return null;
   }





}