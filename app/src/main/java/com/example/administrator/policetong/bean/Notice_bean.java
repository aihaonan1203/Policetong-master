package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/7/25.
 */

public class Notice_bean {

    /**
     * msg : 大吉大利今晚吃鸡
     * detachment : g1
     * date : 2018年6月15日15:17:57
     * grop : 1
     * userid : 101
     */

    private String msg;
    private String detachment;
    private String date;
    private String grop;
    private String userid;

    public Notice_bean(String msg, String detachment, String date, String grop, String userid) {
        this.msg = msg;
        this.detachment = detachment;
        this.date = date;
        this.grop = grop;
        this.userid = userid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetachment() {
        return detachment;
    }

    public void setDetachment(String detachment) {
        this.detachment = detachment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
