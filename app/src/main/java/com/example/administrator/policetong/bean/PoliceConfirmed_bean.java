package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/6.
 */

public class PoliceConfirmed_bean {
    private String userid;
    private String place;
    private String unit;
    private String turn;
    private String date;
    private String other;
    private String latitude;
    private String longitude;


    public PoliceConfirmed_bean(String userid, String place, String unit, String turn, String date, String other, String latitude, String longitude) {
        this.userid = userid;
        this.place = place;
        this.unit = unit;
        this.turn = turn;
        this.date = date;
        this.other = other;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public PoliceConfirmed_bean(String userid, String place, String unit, String turn, String date, String other) {
        this.userid = userid;
        this.place = place;
        this.unit = unit;
        this.turn = turn;
        this.date = date;
        this.other = other;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
