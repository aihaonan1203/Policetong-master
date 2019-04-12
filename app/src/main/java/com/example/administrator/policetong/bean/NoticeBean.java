package com.example.administrator.policetong.bean;

import java.util.List;

public class NoticeBean {


    /**
     * ERRMSG : 成功
     * MsgArray : [{"msg":"出勤岗点:test。执勤人员：张三,。任务内容：jjjjjjj","detachment":"g1","date":"2019年03月26日17时04分17秒","id":"1","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：张三,桂忠,王东,。任务内容：村民   ","detachment":"g1","date":"2019年03月29日10时33分20秒","id":"2","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：潘华,。任务内容：需要","detachment":"g1","date":"2019年03月29日10时33分31秒","id":"3","grop":"1","userid":"103"},{"msg":"出勤岗点:安保警卫。执勤人员：一中队,二中队,三中队,四中队,特勤中队,。任务内容：2018.2123165487/879784654564654354654531321321654654654654651231635465464512313465468487","detachment":"g1","date":"2019年03月29日10时40分17秒","id":"4","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：潘华,。任务内容：村落","detachment":"g1","date":"2019年03月29日10时41分01秒","id":"5","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：潘华,。任务内容：564645646","detachment":"g1","date":"2019年03月29日10时41分40秒","id":"6","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试","detachment":"g1","date":"2019年03月30日10时18分39秒","id":"7","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试1","detachment":"g1","date":"2019年03月30日10时20分31秒","id":"8","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：123123","detachment":"g1","date":"2019年03月30日11时18分58秒","id":"9","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试222222222","detachment":"g1","date":"2019年03月30日11时20分19秒","id":"10","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试--------------------111","detachment":"g1","date":"2019年03月30日11时25分56秒","id":"11","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：1111111111","detachment":"g1","date":"2019年03月30日11时29分47秒","id":"12","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：俞小波,。任务内容：大师傅士大夫大师傅士大夫但是","detachment":"g1","date":"2019年03月30日11时35分03秒","id":"13","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：孙严,俞小波,潘华,。任务内容：就一天一个加工客观","detachment":"g1","date":"2019年04月03日10时55分44秒","id":"14","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：朱晓明,。任务内容：儿童有反应他有反应颜如玉玉玉","detachment":"g1","date":"2019年04月03日11时24分57秒","id":"15","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试111111","detachment":"g1","date":"2019年04月04日14时41分54秒","id":"16","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：2019年04月04日14时41分54秒\t出勤岗点:十里大道。执勤人员：。任务内容：测试111111\r\n2019年04月03日11时24分57秒\t出勤岗点:十里大道。执勤人员：朱\r\n2019年03月30日11时25分56秒\t出勤岗点:十里大道。执勤人员：。任务内容：测试--------------------111","detachment":"g1","date":"2019年04月04日14时42分56秒","id":"17","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试1231123","detachment":"g1","date":"2019年04月04日14时45分40秒","id":"18","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试123123","detachment":"g1","date":"2019年04月04日14时48分12秒","id":"19","grop":"1","userid":"103"},{"msg":"出勤岗点:十里大道。执勤人员：。任务内容：测试===========","detachment":"g1","date":"2019年04月04日15时02分17秒","id":"20","grop":"1","userid":"103"}]
     * RESULT : S
     */

    private String ERRMSG;
    private String RESULT;
    private List<MsgArrayBean> MsgArray;

    public String getERRMSG() {
        return ERRMSG;
    }

    public void setERRMSG(String ERRMSG) {
        this.ERRMSG = ERRMSG;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public List<MsgArrayBean> getMsgArray() {
        return MsgArray;
    }

    public void setMsgArray(List<MsgArrayBean> MsgArray) {
        this.MsgArray = MsgArray;
    }

    public static class MsgArrayBean {
        /**
         * msg : 出勤岗点:test。执勤人员：张三,。任务内容：jjjjjjj
         * detachment : g1
         * date : 2019年03月26日17时04分17秒
         * id : 1
         * grop : 1
         * userid : 103
         */

        private String msg;
        private String detachment;
        private String date;
        private String id;
        private String grop;
        private String userid;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getDetachment() {
            return detachment;
        }

        public void setDetachment(String detachment) {
            this.detachment = detachment;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        @Override
        public String toString() {
            return "MsgArrayBean{" +
                    "msg='" + msg + '\'' +
                    ", detachment='" + detachment + '\'' +
                    ", date='" + date + '\'' +
                    ", id='" + id + '\'' +
                    ", grop='" + grop + '\'' +
                    ", userid='" + userid + '\'' +
                    '}';
        }
    }
}
