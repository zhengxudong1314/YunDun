package com.dahua.searchandwarn.model;

import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/11
 * 功能：
 */

public class SW_SingleWarnBean {

    /**
     * retCode : 0
     * message : null
     * data : {"parentPusher":"0","smallImg":"host00/face/20180514/0e9ee6d7-dec5-482c-9274-4a60d9533761.jpg","faceCardNum":"140203199911111111","controlReason":"无名原因","originalImg":"host00/face/20180514/0af833fc-7e39-4625-aafb-673b25e96bc0.jpg","libName":"在逃库","deviceCode":"5003180000081728022","loglist":[{"recordTime":"2018-05-14 15:18:17","recordPeople":"xzm（重庆市公安局）","recordType":"my","recordMessage":"【告警信息被转发】转发人列表:test"},{"recordTime":"2018-05-14 15:18:17","recordPeople":"xzm（重庆市公安局）","recordType":"my","recordMessage":"【已确认接收预警信息，正在处理中】，让另人处理"},{"recordTime":"2018-05-14 15:17:56","recordPeople":"xzm（重庆市公安局）","recordType":"my","recordMessage":"【已确认接收预警信息，正在处理中】我不处理"},{"recordTime":"2018-03-13 08:09:20","recordPeople":"讯之美研发办公室","recordType":"my","recordMessage":"【产生预警】"}],"deviceName":"讯之美研发办公室","faceName":"张三丰","bigImg":"host00/face/20180514/31e1ce5f-1790-463c-9fba-47150a17fcc7.jpg","similarity":95,"longLabel":"","shortTime":"2018-03-13 08:07:20","controlSimilarity":80,"deviceX":106.6409289837,"shortLabel":"","deviceY":29.4876233024,"saveTime":"2018-03-13 08:09:20","status":"处理中"}
     */

    private int retCode;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * parentPusher : 0
         * smallImg : host00/face/20180514/0e9ee6d7-dec5-482c-9274-4a60d9533761.jpg
         * faceCardNum : 140203199911111111
         * controlReason : 无名原因
         * originalImg : host00/face/20180514/0af833fc-7e39-4625-aafb-673b25e96bc0.jpg
         * libName : 在逃库
         * deviceCode : 5003180000081728022
         * loglist : [{"recordTime":"2018-05-14 15:18:17","recordPeople":"xzm（重庆市公安局）","recordType":"my","recordMessage":"【告警信息被转发】转发人列表:test"},{"recordTime":"2018-05-14 15:18:17","recordPeople":"xzm（重庆市公安局）","recordType":"my","recordMessage":"【已确认接收预警信息，正在处理中】，让另人处理"},{"recordTime":"2018-05-14 15:17:56","recordPeople":"xzm（重庆市公安局）","recordType":"my","recordMessage":"【已确认接收预警信息，正在处理中】我不处理"},{"recordTime":"2018-03-13 08:09:20","recordPeople":"讯之美研发办公室","recordType":"my","recordMessage":"【产生预警】"}]
         * deviceName : 讯之美研发办公室
         * faceName : 张三丰
         * bigImg : host00/face/20180514/31e1ce5f-1790-463c-9fba-47150a17fcc7.jpg
         * similarity : 95
         * longLabel :
         * shortTime : 2018-03-13 08:07:20
         * controlSimilarity : 80
         * deviceX : 106.6409289837
         * shortLabel :
         * deviceY : 29.4876233024
         * saveTime : 2018-03-13 08:09:20
         * status : 处理中
         */

        private String parentPusher;
        private String smallImg;
        private String faceCardNum;
        private String controlReason;
        private String originalImg;
        private String libName;
        private String deviceCode;
        private String deviceName;
        private String faceName;
        private String bigImg;
        private int similarity;
        private String longLabel;
        private String shortTime;
        private int controlSimilarity;
        private double deviceX;
        private String shortLabel;
        private double deviceY;
        private String saveTime;
        private String status;
        private List<LoglistBean> loglist;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<LoglistBean> getLoglist() {
            return loglist;
        }

        public void setLoglist(List<LoglistBean> loglist) {
            this.loglist = loglist;
        }

        public static class LoglistBean {
            /**
             * recordTime : 2018-05-14 15:18:17
             * recordPeople : xzm（重庆市公安局）
             * recordType : my
             * recordMessage : 【告警信息被转发】转发人列表:test
             */

            private String recordTime;
            private String recordPeople;
            private String recordType;
            private String recordMessage;

            public String getRecordTime() {
                return recordTime;
            }

            public void setRecordTime(String recordTime) {
                this.recordTime = recordTime;
            }

            public String getRecordPeople() {
                return recordPeople;
            }

            public void setRecordPeople(String recordPeople) {
                this.recordPeople = recordPeople;
            }

            public String getRecordType() {
                return recordType;
            }

            public void setRecordType(String recordType) {
                this.recordType = recordType;
            }

            public String getRecordMessage() {
                return recordMessage;
            }

            public void setRecordMessage(String recordMessage) {
                this.recordMessage = recordMessage;
            }
        }
    }
}
