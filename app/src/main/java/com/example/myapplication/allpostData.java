package com.example.myapplication;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.net.URL;
public class allpostData {


    @SerializedName("id")
    public int id;
    @SerializedName("post_no")
    public int post_no;
    @SerializedName("post_title")
    public String post_title;
    @SerializedName("post_content")
    public String post_content;
    @SerializedName("post_img") // 게시글 등록 시 필요한 이미지 파일
    public String post_img_file;


    public int getPost_no(){return post_no;}
    public int getId() { return id; }
    public String getPost_content(){return post_content;}
    public String getPost_title() {
        return post_title;
    }
    public String getPost_img_file() {
        return post_img_file;
    }


    public void setPost_content(String post_content) { this.post_content = post_content; }
    public void setPost_no(int post_no) {
        this.post_no = post_no;
    }
    public void setId(int id) { this.id = id; }
    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }
    public void setPost_img_file(String post_img_file) {
        this.post_img_file = post_img_file;
    }

}
