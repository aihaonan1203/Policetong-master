package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/12.
 */

public class Daily_bean {
    private String date;
    private String detachment;
    private String img;
    private String distance;
    private String endtime;
    private String worktype;
    private String begintime;
    private String userid;
    private String content;
    private String road;
    private String forces;
    private String behavior;
    private String grop;

    public Daily_bean(String date, String detachment, String img, String distance, String endtime, String worktype, String begintime, String userid, String content, String road, String forces, String behavior, String grop) {
        this.date = date;
        this.detachment = detachment;
        this.img = img;
        this.distance = distance;
        this.endtime = endtime;
        this.worktype = worktype;
        this.begintime = begintime;
        this.userid = userid;
        this.content = content;
        this.road = road;
        this.forces = forces;
        this.behavior = behavior;
        this.grop = grop;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getWorktype() {
        return worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
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

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getForces() {
        return forces;
    }

    public void setForces(String forces) {
        this.forces = forces;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getGrop() {
        return grop;
    }

    public void setGrop(String grop) {
        this.grop = grop;
    }
}
