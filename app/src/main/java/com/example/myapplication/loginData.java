package com.example.myapplication;

import org.jetbrains.annotations.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class loginData {
    public int id;
    public String user_id;
    public String password;
    public int age;
    public int sex;
    public int isSuccess;

    public int getId() { return id; }
    public String getUser_id(){ return user_id; }
    public String getPassword(){ return password; }
    public int getAge() { return age; }
    public int getSex() { return sex; }
    public int getIsSuccess() { return isSuccess; }

    public void setId(int id) { this.id = id; }
    public void setUser_id(String s){ user_id = s; }
    public void setPassword(String s){ password = s; }
    public void setAge(int age) { this.age = age; }
    public void setSex(int sex) { this.sex = sex; }
    public void setIsSuccess(int isSuccess) { this.isSuccess = isSuccess; }

    public loginData(){

    }
    public loginData(String user_id){
        this.user_id = user_id;
    }
    public loginData(String user_id, String password){
        this.user_id = user_id;
        this.password = password;
    }


}
