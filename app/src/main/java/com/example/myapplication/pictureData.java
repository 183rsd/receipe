package com.example.myapplication;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import java.io.File;
import java.net.URL;

public class pictureData {
    public int pic_no;
    public int pic_user_id;
    public File pic_img;

    public pictureData(){

    }

    public pictureData(int pic_no, int pic_user_id, File pic_img){
        this.pic_no = pic_no;
        this.pic_user_id = pic_user_id;
        this.pic_img = pic_img;
    }

    public int getPic_user_id() {
        return pic_user_id;
    }

    public void setPic_user_id(int pic_user_id) {
        this.pic_user_id = pic_user_id;
    }

    public File getPic_img() {
        return pic_img;
    }

    public void setPic_img(File pic_img) {
        this.pic_img = pic_img;
    }

}
