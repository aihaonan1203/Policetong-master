package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/6.
 */

public class PoliceConfirmed_bean {

    /**
     * id : 21
     * title : dawdadd
     * task_time : 2019-08-11 07:20:00
     * work_time : 2019-08-11 07:20:00
     * content : 1.八里湖北大道与环湖四路由特勤中队负责,需要1人,需要调灯
     2.八里湖北大道与沙阎路信号灯由四中队负责,需要7人,不需调灯
     * remarks :
     * user_id : 超级管理员
     * create_time : 2019-08-11 15:20:15
     */

    private int id;
    private String title;
    private String task_time;
    private String work_time;
    private String content;
    private String remarks;
    private String user_id;
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
