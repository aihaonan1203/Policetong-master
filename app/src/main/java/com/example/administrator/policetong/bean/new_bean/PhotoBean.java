package com.example.administrator.policetong.bean.new_bean;

/**
 * Created by Hjb on 2019/8/2.
 */

public class PhotoBean {

    private String name;
    private String image;


    public PhotoBean(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
