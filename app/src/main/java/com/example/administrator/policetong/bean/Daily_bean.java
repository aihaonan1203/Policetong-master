package com.example.administrator.policetong.bean;

/**
 * Created by Administrator on 2018/6/12.
 */

public class Daily_bean {

    /**
     * id : 6
     * work_time : 2019-08-07
     * start_time : 08:00
     * end_time : 08:00
     * users_id : 孙严,桂忠
     * biroad_id : 长江大道
     * reference : dad
     * bidutytype_id : 警保卫
     * content : 检查车辆X台，其中X多少X台，X多少X台，X多少X台，二三轮车X台，查扣XX台
     * pic : 20190807/e0894eb2d22793cb9656f23ff926b269.JPEG,
     * biillegalacts_id : 禁止货车通行,,
     * user_id : 超级管理员
     * create_time : 2019-08-07 15:25:20
     */

    private int id;
    private String work_time;
    private String start_time;
    private String end_time;
    private String users_id;
    private String biroad_id;
    private String reference;
    private String bidutytype_id;
    private String content;
    private String pic;
    private String biillegalacts_id;
    private String user_id;
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String users_id) {
        this.users_id = users_id;
    }

    public String getBiroad_id() {
        return biroad_id;
    }

    public void setBiroad_id(String biroad_id) {
        this.biroad_id = biroad_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBidutytype_id() {
        return bidutytype_id;
    }

    public void setBidutytype_id(String bidutytype_id) {
        this.bidutytype_id = bidutytype_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBiillegalacts_id() {
        return biillegalacts_id;
    }

    public void setBiillegalacts_id(String biillegalacts_id) {
        this.biillegalacts_id = biillegalacts_id;
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
