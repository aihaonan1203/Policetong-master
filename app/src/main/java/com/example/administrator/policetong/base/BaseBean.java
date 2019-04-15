package com.example.administrator.policetong.base;

public class BaseBean<T> {


    /**
     * code : 0
     * errMsg : SUCCESS
     * data : {"id":2,"userId":"test1","userName":"中队长","squId":2,"sex":"男","tel":"18844556655","duty":"中队长"}
     */

    private int code;
    private String errMsg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
