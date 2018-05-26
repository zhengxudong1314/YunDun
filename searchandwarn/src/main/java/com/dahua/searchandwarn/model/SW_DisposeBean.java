package com.dahua.searchandwarn.model;

/**
 * 创建： ZXD
 * 日期 2018/5/9
 * 功能：
 */

public class SW_DisposeBean {

    /**
     * retCode : 0
     * message : null
     * data : null
     */

    private int retCode;
    private String message;
    private Object data;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
