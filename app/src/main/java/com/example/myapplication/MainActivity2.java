package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("userID");
        String password = intent.getStringExtra("userPass");
        String age = intent.getStringExtra("userAge");
        String sex = intent.getStringExtra("userSex");




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
            } else if (id == R.id.menu_profile){
                fragment = new ProfileFragment();
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
}







