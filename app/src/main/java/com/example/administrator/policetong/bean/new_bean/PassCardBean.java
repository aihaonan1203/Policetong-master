package com.example.administrator.policetong.bean.new_bean;

/**
 * Created by Hjb on 2019/8/1.
 */

public class PassCardBean {

    private int id;
    private int wx_uid;
    private String phone;
    private String name;
    private String startpoint;
    private String endpoint;
    private String addtime;
    private long endtime;
    private long wanttime;
    private int status;
    private int result;
    private String line;
    private long txstarttime;
    private long txendtime;
    private String zpr;
    private int batch;
    private String formId;
    private String failure_cause;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWx_uid() {
        return wx_uid;
    }

    public void setWx_uid(int wx_uid) {
        this.wx_uid = wx_uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public long getWanttime() {
        return wanttime;
    }

    public void setWanttime(long wanttime) {
        this.wanttime = wanttime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public long getTxstarttime() {
        return txstarttime;
    }

    public void setTxstarttime(long txstarttime) {
        this.txstarttime = txstarttime;
    }

    public long getTxendtime() {
        return txendtime;
    }

    public void setTxendtime(long txendtime) {
        this.txendtime = txendtime;
    }

    public String getZpr() {
        return zpr;
    }

    public void setZpr(String zpr) {
        this.zpr = zpr;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFailure_cause() {
        return failure_cause;
    }

    public void setFailure_cause(String failure_cause) {
        this.failure_cause = failure_cause;
    }
}
