package com.example.administrator.policetong.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/6/8.
 */

public class ImageBean {
    private String name;
    private Bitmap bitmap;

    public ImageBean(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
