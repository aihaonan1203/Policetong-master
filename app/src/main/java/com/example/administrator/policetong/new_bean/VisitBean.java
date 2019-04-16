package com.example.administrator.policetong.new_bean;

public class VisitBean {


    /**
     * id : 1
     * userId : test1
     * squId : 2
     * unit : 九江市政府
     * unitNature : 政府单位
     * visitPurpose : 学习1
     * content : 学习十几大精神
     * visitDate : 2019-04-14
     */

    private int id;
    private String userId;
    private int squId;
    private String unit;
    private String unitNature;
    private String visitPurpose;
    private String content;
    private String visitDate;
    private String operate;

    public VisitBean(int id, String userId, int squId, String unit, String unitNature, String visitPurpose, String content, String visitDate, String operate) {
        this.id = id;
        this.userId = userId;
        this.squId = squId;
        this.unit = unit;
        this.unitNature = unitNature;
        this.visitPurpose = visitPurpose;
        this.content = content;
        this.visitDate = visitDate;
        this.operate = operate;
    }

    public VisitBean(String userId, int squId, String unit, String unitNature, String visitPurpose, String content, String visitDate, String operate) {
        this.userId = userId;
        this.squId = squId;
        this.unit = unit;
        this.unitNature = unitNature;
        this.visitPurpose = visitPurpose;
        this.content = content;
        this.visitDate = visitDate;
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

    public int getSquId() {
        return squId;
    }

    public void setSquId(int squId) {
        this.squId = squId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitNature() {
        return unitNature;
    }

    public void setUnitNature(String unitNature) {
        this.unitNature = unitNature;
    }

    public String getVisitPurpose() {
        return visitPurpose;
    }

    public void setVisitPurpose(String visitPurpose) {
        this.visitPurpose = visitPurpose;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
}
