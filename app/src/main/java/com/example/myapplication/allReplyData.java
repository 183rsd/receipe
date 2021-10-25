package com.example.myapplication;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.net.URL;
public class allReplyData {

    public int isSuccess;

    @SerializedName("id")
    public int id;
    @SerializedName("reply_no")
    public int reply_no;
    @SerializedName("post_no")
    public int post_no;
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("user_name")
    public String user_name;
    @SerializedName("reply_content")
    public String reply_content;


    public int getUser_id(){return id;};
    public int getReply_no() { return reply_no; }
    public int getId(){ return id; }
    public int getPost_no() {return post_no;}
    public String getReply_content() { return reply_content; }
    public String getUser_name() { return user_name; }



    public void setReply_no(int reply_no) { this.reply_no = reply_no; }
    public void setPost_no(int post_no){this.post_no = post_no;}
    public void setId(int id) { this.id = id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public void setUser_name(String user_name) { this.user_name = user_name; }
    public void setReply_content(String reply_content) { this.reply_content = reply_content; }


}
