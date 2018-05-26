package com.dahua.searchandwarn.model;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/22
 */

public class SW_DeviceCodeBean {
    private String devCode;
    private String address;

    public SW_DeviceCodeBean() {
    }

    public SW_DeviceCodeBean(String devCode, String address) {
        this.devCode = devCode;
        this.address = address;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SW_DeviceCodeBean{" +
                "devCode='" + devCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
