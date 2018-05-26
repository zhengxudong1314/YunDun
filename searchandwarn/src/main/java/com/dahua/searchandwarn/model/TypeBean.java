package com.dahua.searchandwarn.model;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/22
 */

public class TypeBean {
    private String errer;

    private int position;

    public TypeBean() {
    }

    public String getErrer() {
        return errer;
    }

    public void setErrer(String errer) {
        this.errer = errer;
    }


    public TypeBean(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
