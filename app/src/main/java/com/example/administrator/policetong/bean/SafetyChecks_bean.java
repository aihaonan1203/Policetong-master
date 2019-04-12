package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/10.
 */

public class SafetyChecks_bean {

    /**
     * date : 2018-06-10 09:14:42
     * detachment : 2
     * img : 1012018-06-10-09-15-10.jpg|
     * rectifydate : 2018-06-10 09:14:42
     * distance : 5公里
     * fdunit : 九江职业大学
     * asunit : 九江职业大学
     * userid : 101
     * rectify : 是
     * modify : 未修改
     * road : 濂溪大道
     * report : 是
     * grop : 1
     */

    private String date;
    private String detachment;
    private String img;
    private String rectifydate;
    private String distance;
    private String fdunit;
    private String asunit;
    private String userid;
    private String rectify;
    private String road;
    private String report;
    private String grop;


    public SafetyChecks_bean(String date, String detachment, String img, String rectifydate, String distance, String fdunit, String asunit, String userid, String rectify, String road, String report, String grop) {
        this.date = date;
        this.detachment = detachment;
        this.img = img;
        this.rectifydate = rectifydate;
        this.distance = distance;
        this.fdunit = fdunit;
        this.asunit = asunit;
        this.userid = userid;
        this.rectify = rectify;
        this.road = road;
        this.report = report;
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

    public String getRectifydate() {
        return rectifydate;
    }

    public void setRectifydate(String rectifydate) {
        this.rectifydate = rectifydate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFdunit() {
        return fdunit;
    }

    public void setFdunit(String fdunit) {
        this.fdunit = fdunit;
    }

    public String getAsunit() {
        return asunit;
    }

    public void setAsunit(String asunit) {
        this.asunit = asunit;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRectify() {
        return rectify;
    }

    public void setRectify(String rectify) {
        this.rectify = rectify;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getGrop() {
        return grop;
    }

    public void setGrop(String grop) {
        this.grop = grop;
    }
}
