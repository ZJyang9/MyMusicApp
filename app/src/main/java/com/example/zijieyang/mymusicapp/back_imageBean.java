package com.example.zijieyang.mymusicapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class back_imageBean {
    /*private int backImageResId;//图像资源ID

    public back_imageBean(int backImageResId) {
        this.backImageResId = backImageResId;
    }
    public void setImages(int backImageResId) {
        this.backImageResId = backImageResId;
    }

    public int getImages() {
        return backImageResId;
    }*/
    private String backImageRes;//图像资源ID

    public back_imageBean(String backImageRes) {
        this.backImageRes = backImageRes;
    }
    public void setImages(String backImageRes) {
        this.backImageRes = backImageRes;
    }

    public String getBackImageRes() {
        return backImageRes;
    }
}
