package com.bjpowernode.crm.commons.bean;

public class ResultObject {
    private String code;
    private String msg;
    private Object obj;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public ResultObject() {
    }

    public ResultObject(String code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public ResultObject(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultObject(String code, Object obj) {
        this.code = code;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "ResultObject{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                '}';
    }
}
