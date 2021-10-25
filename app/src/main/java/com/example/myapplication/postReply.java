package com.example.myapplication;

import org.jetbrains.annotations.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class postReply {

    // POST로 전송할 값
    public int post_no;
    public int user_id;
    public String reply_content;

    public int id;
    public int reply_no;
    public String user_name;

    public int getId() { return id; }
    public int getReply_no() { return reply_no; }
    public int getPost_no() { return post_no; }
    public int getUser_id() { return user_id; }
    public String getREPLY_CONTENT(){return reply_content;};
    public String getUser_name(){return user_name;}

    public postReply(int post_no, int user_id, String reply_content){
        this.post_no = post_no;
        this.user_id = user_id;
        this.reply_content = reply_content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReply_no(int reply_no) {
        this.reply_no = reply_no;
    }

    public void setPost_no(int post_no) {
        this.post_no = post_no;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setREPLY_CONTENT(String reply_content) {
        this.reply_content = reply_content;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}