package com.example.administrator.policetong.new_bean;

public class JingBaoBean {


    /**
     * id : 1
     * userId : test1
     * title : 植树活动
     * description : 植树节
     * taskLeader : 大队长
     * taskDate : 2019-04-06
     * dutyTime : 09:14:44
     * roadMap : 这里到那里
     * policeMent : 一中队
     * other : 没啥
     */

    private int id;
    private String userId;
    private String title;
    private String description;
    private String taskLeader;
    private String taskDate;
    private String dutyTime;
    private String roadMap;
    private String policeMent;
    private String other;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskLeader() {
        return taskLeader;
    }

    public void setTaskLeader(String taskLeader) {
        this.taskLeader = taskLeader;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(String dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getRoadMap() {
        return roadMap;
    }

    public void setRoadMap(String roadMap) {
        this.roadMap = roadMap;
    }

    public String getPoliceMent() {
        return policeMent;
    }

    public void setPoliceMent(String policeMent) {
        this.policeMent = policeMent;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
