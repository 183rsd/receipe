package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity  {

    private BottomNavigationView mBottomNV;
    public String success_user_id, success_password;
    public int success_id, success_age, success_sex;
    Bundle bundle = new Bundle(); // 프로필로 가는 번들
    Bundle bundle_pic = new Bundle(); // 레시피로 가는 번들
    private BackPressHandler backPressHandler = new BackPressHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        success_id = intent.getIntExtra("id",0);
        success_user_id = intent.getStringExtra("user_id");
        success_password = intent.getStringExtra("password");
        success_age = intent.getIntExtra("age",1000);
        success_sex = intent.getIntExtra("sex",1000);


        bundle.putInt("id",success_id);
        bundle.putString("user_id", success_user_id);
        bundle.putString("user_pw", success_password);
        bundle.putInt("user_age",success_age);
        bundle.putInt("user_sex",success_sex);


        bundle_pic.putInt("id",success_id);



        mBottomNV = findViewById(R.id.bottom_nav);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                BottomNavigate(menuitem.getItemId());

                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.menu_home);
    }
    private void BottomNavigate(int id){
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if(currentFragment != null){
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if(fragment == null){
            if(id == R.id.menu_home){
                fragment = new HomeFragment();
                fragment.setArguments(bundle_pic);
            } else if (id == R.id.menu_profile){
                fragment = new ProfileFragment();
                fragment.setArguments(bundle);
            } else if (id == R.id.menu_list){
                fragment = new ListFragment();
            } else {
                fragment = new RankFragment();
            }
            fragmentTransaction.add(R.id.fragment_main, fragment, tag);

        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed(){
        backPressHandler.onBackPressed("'뒤로' 버튼 한번 더 누르시면 종료됩니다.",3000);
    }

}







