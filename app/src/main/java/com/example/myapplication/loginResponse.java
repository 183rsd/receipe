package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class loginResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("user_id")
    public  String user_id;
    @SerializedName("password")
    public String password;
    @SerializedName("age")
    public int age;
    @SerializedName("sex")
    public int sex;
    @SerializedName("isSuccess")
    public int isSuccess;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getSex() { return sex; }
    public void setSex(int sex) { this.sex = sex; }

    public int getIsSuccess() { return isSuccess; }
    public void setIsSuccess(int isSuccess) { this.isSuccess = isSuccess; }
}
