package com.example.administrator.policetong.bean;

public class PoliceMent {


    /**
     * teemId : 1
     * place : 八里湖大道与兴城大道路口
     * squName : 三中队
     * beginTime : 111221
     * numer : 2人
     */

    private int teemId;
    private String place;
    private String squName;
    private int beginTime;
    private String numer;
    private String diaodeng;

    public PoliceMent(int teemId, String place, String squName, int beginTime, String numer, String diaodeng) {
        this.teemId = teemId;
        this.place = place;
        this.squName = squName;
        this.beginTime = beginTime;
        this.numer = numer;
        this.diaodeng = diaodeng;
    }

    public String getDiaodeng() {
        return diaodeng;
    }

    public void setDiaodeng(String diaodeng) {
        this.diaodeng = diaodeng;
    }

    public int getTeemId() {
        return teemId;
    }

    public void setTeemId(int teemId) {
        this.teemId = teemId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSquName() {
        return squName;
    }

    public void setSquName(String squName) {
        this.squName = squName;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }
}
