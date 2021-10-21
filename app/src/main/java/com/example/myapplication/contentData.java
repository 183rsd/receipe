package com.example.myapplication;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.net.URL;
public class contentData {
    @SerializedName("post_no")
    public int post_no;
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("post_content")
    public String post_content;
    @SerializedName("post_title")
    public String post_title;
//    @SerializedName("post_img") // 게시판 출력 시 작은 이미지
//    public String post_img;
    @SerializedName("post_img") // 게시글 등록 시 필요한 이미지 파일
    public File post_img_file;
//    @SerializedName("post_img_uri")
//    public Uri post_img_uri;

    public int getPost_no(){return post_no;}
    public String getUser_id(){return user_id;}
    public String getPost_content(){return post_content;}
    public String getPost_title() {
        return post_title;
    }
//    public String getPost_img() {
//        return post_img;
//    }
    public File getPost_img_file() {
        return post_img_file;
    }
//    public Uri getPost_img_uri() {return post_img_uri;}

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }
//    public void setPost_img(String post_img) {
//        this.post_img = post_img;
//    }
    public void setPost_no(int post_no) {
        this.post_no = post_no;
    }
    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public void setPost_img_file(File post_img_file) {
        this.post_img_file = post_img_file;
    }
//    public void setPost_img_uri(Uri post_img_uri) {this.post_img_uri = post_img_uri;}
}
