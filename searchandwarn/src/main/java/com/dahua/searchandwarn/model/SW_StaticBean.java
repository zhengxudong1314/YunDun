package com.dahua.searchandwarn.model;

import java.io.Serializable;
import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/15
 * 功能：
 */

public class SW_StaticBean {

    /**
     * data : [{"venderName":"海康","face":"http://10.209.0.210:6120/pic?=d8=ia14z3bc8s315-745m1ep=t4i0=*1p4i=d1s*i2d11*6i2d3=*db1da29c1-eec221f-1920ee-65i553*e892fd7","cardNum":"T1201081500002017101015_1","libId":"1a4f18feacb341959ded9733e4aaa50a","controlReason":"","peopleId":"1964c0631c7344aa94974a8c2a753c41","similarity":0.9997082948684692,"longLabel":"","libName":"静态人脸库","name":"T1201081500002017101015_1","shortLabel":""}]
     * retCode : 0
     * message : 静态比对成功
     */

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

    public static class DataBean implements Serializable{
        /**
         * venderName : 海康
         * face : http://10.209.0.210:6120/pic?=d8=ia14z3bc8s315-745m1ep=t4i0=*1p4i=d1s*i2d11*6i2d3=*db1da29c1-eec221f-1920ee-65i553*e892fd7
         * cardNum : T1201081500002017101015_1
         * libId : 1a4f18feacb341959ded9733e4aaa50a
         * controlReason :
         * peopleId : 1964c0631c7344aa94974a8c2a753c41
         * similarity : 0.9997082948684692
         * longLabel :
         * libName : 静态人脸库
         * name : T1201081500002017101015_1
         * shortLabel :
         */

        private String venderName;
        private String face;
        private String cardNum;
        private String libId;
        private String controlReason;
        private String peopleId;
        private double similarity;
        private String longLabel;
        private String libName;
        private String name;
        private String shortLabel;

        public String getVenderName() {
            return venderName;
        }

        public void setVenderName(String venderName) {
            this.venderName = venderName;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public String getLibId() {
            return libId;
        }

        public void setLibId(String libId) {
            this.libId = libId;
        }

        public String getControlReason() {
            return controlReason;
        }

        public void setControlReason(String controlReason) {
            this.controlReason = controlReason;
        }

        public String getPeopleId() {
            return peopleId;
        }

        public void setPeopleId(String peopleId) {
            this.peopleId = peopleId;
        }

        public double getSimilarity() {
            return similarity;
        }

        public void setSimilarity(double similarity) {
            this.similarity = similarity;
        }

        public String getLongLabel() {
            return longLabel;
        }

        public void setLongLabel(String longLabel) {
            this.longLabel = longLabel;
        }

        public String getLibName() {
            return libName;
        }

        public void setLibName(String libName) {
            this.libName = libName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortLabel() {
            return shortLabel;
        }

        public void setShortLabel(String shortLabel) {
            this.shortLabel = shortLabel;
        }
    }
}
