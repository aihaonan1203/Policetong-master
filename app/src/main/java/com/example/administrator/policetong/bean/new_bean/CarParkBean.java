package com.example.administrator.policetong.bean.new_bean;

import java.util.ArrayList;

public class CarParkBean {

    /**
     * id : 7
     * ownername : 吴献伟
     * tel : 吴献伟
     * parkid : 八里湖北大桥下停车场
     * plateno : 无
     * typeid : 三轮电动车
     * engineno : 无
     * frameno : 无
     * intime : 2019-08-13 03:39:18
     * indesc : 超载
     * inuid : 徐倩玲
     * inpic : ["20190813/d88047dbd51263629822b853f7df1c60.JPEG"]
     * outtime :
     * outdesc :
     * outuid :
     * voucher :
     * outpic :
     * isdel : 0
     * status : 0
     */

    private int id;
    private String ownername;
    private String tel;
    private String parkid;
    private String plateno;
    private String typeid;
    private String engineno;
    private String frameno;
    private String intime;
    private String indesc;
    private String inuid;
    private String outtime;
    private String outdesc;
    private String outuid;
    private String voucher;
    private ArrayList<String> outpic;
    private int isdel;
    private int status;
    private ArrayList<String> inpic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getParkid() {
        return parkid;
    }

    public void setParkid(String parkid) {
        this.parkid = parkid;
    }

    public String getPlateno() {
        return plateno;
    }

    public void setPlateno(String plateno) {
        this.plateno = plateno;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getEngineno() {
        return engineno;
    }

    public void setEngineno(String engineno) {
        this.engineno = engineno;
    }

    public String getFrameno() {
        return frameno;
    }

    public void setFrameno(String frameno) {
        this.frameno = frameno;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getIndesc() {
        return indesc;
    }

    public void setIndesc(String indesc) {
        this.indesc = indesc;
    }

    public String getInuid() {
        return inuid;
    }

    public void setInuid(String inuid) {
        this.inuid = inuid;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getOutdesc() {
        return outdesc;
    }

    public void setOutdesc(String outdesc) {
        this.outdesc = outdesc;
    }

    public String getOutuid() {
        return outuid;
    }

    public void setOutuid(String outuid) {
        this.outuid = outuid;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public ArrayList<String> getOutpic() {
        return outpic;
    }

    public void setOutpic(ArrayList<String> outpic) {
        this.outpic = outpic;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<String> getInpic() {
        return inpic;
    }

    public void setInpic(ArrayList<String> inpic) {
        this.inpic = inpic;
    }
}
