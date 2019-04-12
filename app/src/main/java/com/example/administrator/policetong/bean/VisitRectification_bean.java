package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/11.
 */

public class VisitRectification_bean {

    /**
     * date : null
     * detachment : null
     * modify : 未修改
     * unit : 某
     * unitnature : 政府机关
     * img : 1012018-06-11-10-26-13.jpg/
     * grop : null
     * userid : 101
     * content : 定位
     * objective : 一般性走访
     */

    private String date;
    private String detachment;
    private String unit;
    private String unitnature;
    private String img;
    private String grop;
    private String userid;
    private String content;
    private String objective;

    public VisitRectification_bean(String date, String detachment, String unit, String unitnature, String img, String grop, String userid, String content, String objective) {
        this.date = date;
        this.detachment = detachment;
        this.unit = unit;
        this.unitnature = unitnature;
        this.img = img;
        this.grop = grop;
        this.userid = userid;
        this.content = content;
        this.objective = objective;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitnature() {
        return unitnature;
    }

    public void setUnitnature(String unitnature) {
        this.unitnature = unitnature;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }
}
