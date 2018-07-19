package com.dahua.searchandwarn.model;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/22
 */

public class SW_TypeBean {
    private String errer;

    private String position;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SW_TypeBean(String position,String status) {
        this.position = position;
        this.status = status;
    }

    public String getErrer() {
        return errer;
    }

    public void setErrer(String errer) {
        this.errer = errer;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
