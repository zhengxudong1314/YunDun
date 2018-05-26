package com.dahua.searchandwarn.model;

import java.io.Serializable;
import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/11
 * 功能：
 */

public class SW_HistoryWarnBean {


    private int retCode;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private String parentPusher;
        private String smallImg;
        private String objBottom;
        private String faceCardNum;
        private String controlReason;
        private String originalImg;
        private String libName;
        private String deviceCode;
        private String deviceName;
        private String faceName;
        private String bigImg;
        private int similarity;
        private String objLeft;
        private String longLabel;
        private String shortTime;
        private String alarmId;
        private int controlSimilarity;
        private double deviceX;
        private String shortLabel;
        private double deviceY;
        private String saveTime;
        private String objRight;
        private String objTop;
        private String status;
        private boolean isRead;

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public String getParentPusher() {
            return parentPusher;
        }

        public void setParentPusher(String parentPusher) {
            this.parentPusher = parentPusher;
        }

        public String getSmallImg() {
            return smallImg;
        }

        public void setSmallImg(String smallImg) {
            this.smallImg = smallImg;
        }

        public String getObjBottom() {
            return objBottom;
        }

        public void setObjBottom(String objBottom) {
            this.objBottom = objBottom;
        }

        public String getFaceCardNum() {
            return faceCardNum;
        }

        public void setFaceCardNum(String faceCardNum) {
            this.faceCardNum = faceCardNum;
        }

        public String getControlReason() {
            return controlReason;
        }

        public void setControlReason(String controlReason) {
            this.controlReason = controlReason;
        }

        public String getOriginalImg() {
            return originalImg;
        }

        public void setOriginalImg(String originalImg) {
            this.originalImg = originalImg;
        }

        public String getLibName() {
            return libName;
        }

        public void setLibName(String libName) {
            this.libName = libName;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getFaceName() {
            return faceName;
        }

        public void setFaceName(String faceName) {
            this.faceName = faceName;
        }

        public String getBigImg() {
            return bigImg;
        }

        public void setBigImg(String bigImg) {
            this.bigImg = bigImg;
        }

        public int getSimilarity() {
            return similarity;
        }

        public void setSimilarity(int similarity) {
            this.similarity = similarity;
        }

        public String getObjLeft() {
            return objLeft;
        }

        public void setObjLeft(String objLeft) {
            this.objLeft = objLeft;
        }

        public String getLongLabel() {
            return longLabel;
        }

        public void setLongLabel(String longLabel) {
            this.longLabel = longLabel;
        }

        public String getShortTime() {
            return shortTime;
        }

        public void setShortTime(String shortTime) {
            this.shortTime = shortTime;
        }

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public int getControlSimilarity() {
            return controlSimilarity;
        }

        public void setControlSimilarity(int controlSimilarity) {
            this.controlSimilarity = controlSimilarity;
        }

        public double getDeviceX() {
            return deviceX;
        }

        public void setDeviceX(double deviceX) {
            this.deviceX = deviceX;
        }

        public String getShortLabel() {
            return shortLabel;
        }

        public void setShortLabel(String shortLabel) {
            this.shortLabel = shortLabel;
        }

        public double getDeviceY() {
            return deviceY;
        }

        public void setDeviceY(double deviceY) {
            this.deviceY = deviceY;
        }

        public String getSaveTime() {
            return saveTime;
        }

        public void setSaveTime(String saveTime) {
            this.saveTime = saveTime;
        }

        public String getObjRight() {
            return objRight;
        }

        public void setObjRight(String objRight) {
            this.objRight = objRight;
        }

        public String getObjTop() {
            return objTop;
        }

        public void setObjTop(String objTop) {
            this.objTop = objTop;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


    }

}
