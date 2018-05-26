package com.dahua.searchandwarn.model;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/25
 */

public class SW_FaceParams {
    private String similarity;
    private String startTime;
    private String endTime;
    private String imageBase64;
    private String deviceCodes;
    private String operator;

    public String getDeviceCodes() {
        return deviceCodes;
    }

    public void setDeviceCodes(String deviceCodes) {
        this.deviceCodes = deviceCodes;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
