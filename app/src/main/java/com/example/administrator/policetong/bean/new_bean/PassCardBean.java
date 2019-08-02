package com.example.administrator.policetong.bean.new_bean;

/**
 * Created by Hjb on 2019/8/1.
 */

public class PassCardBean {


    /**
     * id : 183
     * phone : 13545551885
     * name : 朱必强
     * startpoint : 文愽大道
     * endpoint : 八里湖高速口
     * addtime : 1564624022
     * wanttime : 2019年08月01日
     * status : 3
     * result : 2
     * line :
     * txstarttime :
     * txendtime :
     * zpr : 2
     * failure_cause : 需提起一个工作日的16时之前提交申请
     */

    private int id;
    private String phone;
    private String name;
    private String startpoint;
    private String endpoint;
    private String addtime;
    private String wanttime;
    private int status;
    private int result;
    private String line;
    private String txstarttime;
    private String txendtime;
    private int zpr;
    private String failure_cause;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getWanttime() {
        return wanttime;
    }

    public void setWanttime(String wanttime) {
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

    public String getTxstarttime() {
        return txstarttime;
    }

    public void setTxstarttime(String txstarttime) {
        this.txstarttime = txstarttime;
    }

    public String getTxendtime() {
        return txendtime;
    }

    public void setTxendtime(String txendtime) {
        this.txendtime = txendtime;
    }

    public int getZpr() {
        return zpr;
    }

    public void setZpr(int zpr) {
        this.zpr = zpr;
    }

    public String getFailure_cause() {
        return failure_cause;
    }

    public void setFailure_cause(String failure_cause) {
        this.failure_cause = failure_cause;
    }
}
