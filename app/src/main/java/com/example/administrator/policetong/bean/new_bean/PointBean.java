package com.example.administrator.policetong.bean.new_bean;

/**
 * Created by Hjb on 2019/8/2.
 */

public class PointBean {

    private int id;
    private String name;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
