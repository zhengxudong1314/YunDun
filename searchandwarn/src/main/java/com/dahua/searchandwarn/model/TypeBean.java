package com.dahua.searchandwarn.model;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/22
 */

public class TypeBean {
    private String errer;

    private String position;

    public TypeBean(String position) {
        this.position = position;
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
