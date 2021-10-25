package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestItem_reply {
    @SerializedName("isSuccess")
    public int isSuccess;
    @SerializedName("results")
    public List<allReplyData> results;

    @Override
    public String toString(){ // 로그찍어보기 위한 용도
        return "TestItem_reply{"+"results="+results+"}";
    }
}