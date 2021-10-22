package com.example.myapplication;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.net.URL;
public class postcreateData {
    // POST로 전송할 값
    public int send_user_id;
    public String send_post_title;
    public String send_post_content;
    public File send_post_img;

    // POST로 전달받을 값
    @SerializedName("post_no")
    public int post_no;
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("post_content")
    public String post_content;
    @SerializedName("post_title")
    public String post_title;
    @SerializedName("post_img") // 게시글 등록 시 필요한 이미지 파일
    public String post_img;
    @SerializedName("img_string")
    public String img_string;

    public postcreateData(int send_user_id, String send_post_content, String send_post_title, File send_post_img){
        this.send_user_id = send_user_id;
        this.send_post_content = send_post_content;
        this.send_post_title = send_post_title;
        this.send_post_img = send_post_img;
    }



    public int getPost_no(){return post_no;}
    public int getUser_id(){return user_id;}
    public String getPost_content(){return post_content;}
    public String getPost_title() {
        return post_title;
    }
    public String getPost_img() {
        return post_img;
    }
    public String getImg_string() {
        return img_string;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }
    public void setPost_no(int post_no) {
        this.post_no = post_no;
    }
    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }
    public void setImg_string(String img_string) {
        this.img_string = img_string;
    }
}
