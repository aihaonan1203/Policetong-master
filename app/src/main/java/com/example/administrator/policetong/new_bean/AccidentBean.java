package com.example.administrator.policetong.new_bean;

public class AccidentBean {


    /**
     * id : 1
     * userId : test1
     * date : 2019-04-14
     * longitude : 32.23
     * latitude : 115.25
     * type : 撞车
     * participant : 参与方
     * carType : 电动车
     * hurtType : 骨折
     * carAmage : 报废
     */

    private int id;
    private String userId;
    private String date;
    private String longitude;
    private String latitude;
    private String type;
    private String participant;
    private String carType;
    private String hurtType;
    private String carAmage;
    private String operate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getHurtType() {
        return hurtType;
    }

    public void setHurtType(String hurtType) {
        this.hurtType = hurtType;
    }

    public String getCarAmage() {
        return carAmage;
    }

    public void setCarAmage(String carAmage) {
        this.carAmage = carAmage;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
