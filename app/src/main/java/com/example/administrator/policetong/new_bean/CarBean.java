package com.example.administrator.policetong.new_bean;

public class CarBean {


    /**
     * id : 1
     * userId : test1
     * entranceTime : 1555259137
     * models : SUV
     * licence : 赣G888888
     * type : 临时停车
     * remark : 备注：外来人员
     * leaveTime : 1555261548
     * leaveLicense : 同意放行
     * status : 0
     */

    private int id;
    private String userId;
    private Long entranceTime;
    private String models;
    private String licence;
    private String type;
    private String remark;
    private Long leaveTime;
    private String leaveLicense;
    private int status;
    private String operate;

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Long getEntranceTime() {
        return entranceTime;
    }

    public void setEntranceTime(Long entranceTime) {
        this.entranceTime = entranceTime;
    }

    public Long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Long leaveTime) {
        this.leaveTime = leaveTime;
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



    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public String getLeaveLicense() {
        return leaveLicense;
    }

    public void setLeaveLicense(String leaveLicense) {
        this.leaveLicense = leaveLicense;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
