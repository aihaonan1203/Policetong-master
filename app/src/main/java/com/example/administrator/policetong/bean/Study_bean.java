package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/8/9.
 */

public class Study_bean {

    /**
     * date : 2018年08月09日 06:43:41
     * detachment : 1
     * modify : 未修改
     * grop : 1
     * userid : 101
     * content : ss
     * username : 张三
     */

    private String date;
    private String detachment;
    private String modify;
    private String grop;
    private String userid;
    private String content;
    private String username;

    public Study_bean(String date, String detachment, String grop, String userid, String content, String username) {
        this.date = date;
        this.detachment = detachment;
        this.grop = grop;
        this.userid = userid;
        this.content = content;
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetachment() {
        return detachment;
    }

    public void setDetachment(String detachment) {
        this.detachment = detachment;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public String getGrop() {
        return grop;
    }

    public void setGrop(String grop) {
        this.grop = grop;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
