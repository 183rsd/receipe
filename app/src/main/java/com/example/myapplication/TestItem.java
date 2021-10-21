package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestItem {
    @SerializedName("isSuccess")
    public int isSuccess;
    @SerializedName("results")
    public List<allpostData> results;

    @Override
    public String toString(){ // 로그찍어보기 위한 용도
        return "TestItem{"+"results="+results+"}";
    }
}
