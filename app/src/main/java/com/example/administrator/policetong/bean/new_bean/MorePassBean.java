package com.example.administrator.policetong.bean.new_bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hjb on 2019/8/2.
 */

public class MorePassBean {


    private TxzformBean txzform;
    private List<CarDateBean> car_date;

    public TxzformBean getTxzform() {
        return txzform;
    }

    public void setTxzform(TxzformBean txzform) {
        this.txzform = txzform;
    }

    public List<CarDateBean> getCar_date() {
        return car_date;
    }

    public void setCar_date(List<CarDateBean> car_date) {
        this.car_date = car_date;
    }

    public static class TxzformBean {


        private int id;
        private String phone;
        private String name;
        private String startpoint;
        private String endpoint;
        private String addtime;
        private long endtime;
        private String wanttime;
        private int status;
        private int result;
        private String txstarttime;
        private String txendtime;
        private String failure_cause;
        private String zprname;
        private String linelist;

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

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
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

        public String getFailure_cause() {
            return failure_cause;
        }

        public void setFailure_cause(String failure_cause) {
            this.failure_cause = failure_cause;
        }

        public String getZprname() {
            return zprname;
        }

        public void setZprname(String zprname) {
            this.zprname = zprname;
        }

        public String getLinelist() {
            return linelist;
        }

        public void setLinelist(String linelist) {
            this.linelist = linelist;
        }
    }

    public static class CarDateBean implements Serializable{
        /**
         * id : 179
         * carno : 后改密码
         * hwid : 1
         * jszpic : 20190802/639add326d013581b6815fc19cc341e1.jpg
         * xszpic : 20190802/bc36e0895a2f7940e56e0b5e6c81ae4a.jpg
         * bxpic : 20190802/c439321b82455e733323a00077f3c365.jpg
         * clpic : 20190802/c5001f6ddbdb7d41d14725c4a907501e.jpg
         * cwpic : 20190802/edbdd59df3e4c3c5f5ea64e8872a3023.jpg
         * zcpic : 20190802/d6ea4c41f16556804f7d9f75921b71bb.jpg
         * ycpic : 20190802/0507cedce8610a064c1ab0e816253fc9.jpg
         * nbpic : 20190802/69fef16ab827abc8440c3e7d5b0babf2.jpg
         * result : 0
         * huowuname : 生鲜
         */

        private int id;
        private String carno;
        private int hwid;
        private String jszpic;
        private String xszpic;
        private String bxpic;
        private String clpic;
        private String cwpic;
        private String zcpic;
        private String ycpic;
        private String nbpic;
        private int result;
        private String huowuname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCarno() {
            return carno;
        }

        public void setCarno(String carno) {
            this.carno = carno;
        }

        public int getHwid() {
            return hwid;
        }

        public void setHwid(int hwid) {
            this.hwid = hwid;
        }

        public String getJszpic() {
            return jszpic;
        }

        public void setJszpic(String jszpic) {
            this.jszpic = jszpic;
        }

        public String getXszpic() {
            return xszpic;
        }

        public void setXszpic(String xszpic) {
            this.xszpic = xszpic;
        }

        public String getBxpic() {
            return bxpic;
        }

        public void setBxpic(String bxpic) {
            this.bxpic = bxpic;
        }

        public String getClpic() {
            return clpic;
        }

        public void setClpic(String clpic) {
            this.clpic = clpic;
        }

        public String getCwpic() {
            return cwpic;
        }

        public void setCwpic(String cwpic) {
            this.cwpic = cwpic;
        }

        public String getZcpic() {
            return zcpic;
        }

        public void setZcpic(String zcpic) {
            this.zcpic = zcpic;
        }

        public String getYcpic() {
            return ycpic;
        }

        public void setYcpic(String ycpic) {
            this.ycpic = ycpic;
        }

        public String getNbpic() {
            return nbpic;
        }

        public void setNbpic(String nbpic) {
            this.nbpic = nbpic;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getHuowuname() {
            return huowuname;
        }

        public void setHuowuname(String huowuname) {
            this.huowuname = huowuname;
        }
    }
}
