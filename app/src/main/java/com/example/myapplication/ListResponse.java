package com.example.myapplication;
import com.google.gson.annotations.SerializedName;
public class ListResponse {
    @SerializedName("post_no")
    public int post_no;
    @SerializedName("user_id")
    public  String user_id;
    @SerializedName("post_content")
    public String post_content;
    @SerializedName("post_title")
    public String post_title;
    @SerializedName("reply_no")
    public int reply_no;
    @SerializedName("reply_content")
    public String reply_content;
    @SerializedName("isSuccess")
    public int isSuccess;
    public int getPost_no() { return post_no; }
    public void setPost_no(int id) { this.post_no = post_no; }
    public int getReply_no() { return reply_no; }
    public void setReply_no(int id) { this.reply_no = reply_no; }
    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getPost_content() { return post_content; }
    public void setPost_content(String post_content) { this.post_content = post_content; }
    public String getPost_title() { return post_title; }
    public void setPost_title(String post_title) { this.post_title = post_title; }
    public String getPost_ReplyContent() { return reply_content; }
    public void setPost_ReplyContent(String post_content) { this.reply_content = post_content; }
    public int getIsSuccess() { return isSuccess; }
    public void setIsSuccess(int isSuccess) { this.isSuccess = isSuccess; }
}
