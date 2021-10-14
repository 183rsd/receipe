package com.example.myapplication;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.net.URL;

public class pictureData {
    // POST로 전송할 값
    public int pic_no;
    public int pic_user_id;
    public File pic_img;


    // POST로 반환받을 값
    @SerializedName("rec_no")
    public int rec_no;
    @SerializedName("rec_name")
    public String rec_name;
    @SerializedName("rec_content")
    public String rec_content;
    @SerializedName("rec_ingre")
    public String rec_ingre;

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

    public int getRec_no() { return rec_no; }

    public void setRec_no(int rec_no) { this.rec_no = rec_no; }

    public String getRec_name() { return rec_name; }

    public void setRec_name(String rec_name) { this.rec_name = rec_name; }

    public String getRec_content() { return rec_content; }

    public void setRec_content(String rec_content) { this.rec_content = rec_content; }

    public String getRec_ingre() { return rec_ingre; }

    public void setRec_ingre(String rec_ingre) { this.rec_ingre = rec_ingre; }

}
