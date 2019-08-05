package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/13.
 */

public class Road_bean {


    /**
     * id : 6
     * road_id : 春江路
     * biroadtype_id : 村村通到路
     * biroadgrade_id : 沥青
     * pavement : 损毁严重
     * startpoint : 1000
     * endpoint : 1000
     * user_id : 超级管理员
     * mileage : 100000
     * create_time : 2019-08-04 20:11:53
     */

    private int id;
    private String road_id;
    private String biroadtype_id;
    private String biroadgrade_id;
    private String pavement;
    private String startpoint;
    private String endpoint;
    private String user_id;
    private String mileage;
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoad_id() {
        return road_id;
    }

    public void setRoad_id(String road_id) {
        this.road_id = road_id;
    }

    public String getBiroadtype_id() {
        return biroadtype_id;
    }

    public void setBiroadtype_id(String biroadtype_id) {
        this.biroadtype_id = biroadtype_id;
    }

    public String getBiroadgrade_id() {
        return biroadgrade_id;
    }

    public void setBiroadgrade_id(String biroadgrade_id) {
        this.biroadgrade_id = biroadgrade_id;
    }

    public String getPavement() {
        return pavement;
    }

    public void setPavement(String pavement) {
        this.pavement = pavement;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
