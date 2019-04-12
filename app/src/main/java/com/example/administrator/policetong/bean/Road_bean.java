package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/13.
 */

public class Road_bean {

    /**
     * pavement : 1
     * date : 2018年06月13日 02:15:22
     * detachment : 2
     * modify : 未修改
     * distance : awdwd
     * nature : 城市道路
     * grade : awda
     * name : dawd
     * start : awdawd
     * end : dwad
     * grop : 1
     * userid : 101
     */

    private String pavement;
    private String date;
    private String detachment;
    private String modify;
    private String distance;
    private String nature;
    private String grade;
    private String name;
    private String start;
    private String end;
    private String grop;
    private String userid;

    public Road_bean(String pavement, String date, String detachment, String distance, String nature, String grade, String name, String start, String end, String grop, String userid) {
        this.pavement = pavement;
        this.date = date;
        this.detachment = detachment;
        this.distance = distance;
        this.nature = nature;
        this.grade = grade;
        this.name = name;
        this.start = start;
        this.end = end;
        this.grop = grop;
        this.userid = userid;
    }

    public String getPavement() {
        return pavement;
    }

    public void setPavement(String pavement) {
        this.pavement = pavement;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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
