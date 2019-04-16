package com.example.administrator.policetong.new_bean;

public class StudyBean {


    /**
     * id : 1
     * userId : test1
     * content : 测试
     * place : 测试地点
     * studyTime : 2019-04-14
     * squId : 2
     */

    private int id;
    private String userId;
    private String content;
    private String place;
    private String studyTime;
    private int squId;
    private String operate;

    public StudyBean(String userId, String content, String place, String studyTime, int squId, String operate) {
        this.userId = userId;
        this.content = content;
        this.place = place;
        this.studyTime = studyTime;
        this.squId = squId;
        this.operate = operate;
    }

    public StudyBean(int id, String userId, String content, String place, String studyTime, int squId, String operate) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.place = place;
        this.studyTime = studyTime;
        this.squId = squId;
        this.operate = operate;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public int getSquId() {
        return squId;
    }

    public void setSquId(int squId) {
        this.squId = squId;
    }
}
