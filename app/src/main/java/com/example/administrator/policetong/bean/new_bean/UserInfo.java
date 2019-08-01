package com.example.administrator.policetong.bean.new_bean;

import java.util.List;

/**
 * Created by Hjb on 2019/8/1.
 */

public class UserInfo {

    private String token;
    private UserBean user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBean getUser() {
        if (user==null){
            user=new UserBean();
        }
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * uid : 2
         * user : administrator
         * truename : 超级管理员
         * head : null
         * login_count : 431
         * last_login_ip : 117.40.180.2
         * last_login_time : 2019-08-01 08:38:00
         * status : 1
         * updatapassword : 1
         * create_time : null
         * update_time : 2019-08-01 08:38:00
         * tel :
         * group_id : [1]
         * dpt_id : 1
         */

        private int uid;
        private String user;
        private String truename;
        private Object head;
        private int login_count;
        private String last_login_ip;
        private String last_login_time;
        private int status;
        private int updatapassword;
        private Object create_time;
        private String update_time;
        private String tel;
        private int dpt_id;
        private List<Integer> group_id;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public Object getHead() {
            return head;
        }

        public void setHead(Object head) {
            this.head = head;
        }

        public int getLogin_count() {
            return login_count;
        }

        public void setLogin_count(int login_count) {
            this.login_count = login_count;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUpdatapassword() {
            return updatapassword;
        }

        public void setUpdatapassword(int updatapassword) {
            this.updatapassword = updatapassword;
        }

        public Object getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Object create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public int getDpt_id() {
            return dpt_id;
        }

        public void setDpt_id(int dpt_id) {
            this.dpt_id = dpt_id;
        }

        public List<Integer> getGroup_id() {
            return group_id;
        }

        public void setGroup_id(List<Integer> group_id) {
            this.group_id = group_id;
        }
    }
}
