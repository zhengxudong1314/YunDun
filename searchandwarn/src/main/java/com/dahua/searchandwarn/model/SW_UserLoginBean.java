package com.dahua.searchandwarn.model;

/**
 * 创建： ZXD
 * 日期 2018/5/11
 * 功能：
 */

public class SW_UserLoginBean {

    /**
     * retCode : 0
     * message : null
     * data : {"name":null,"successMsg":"登陆成功！","account":"yundunProapp"}
     */
    public static final String USERNANE = "研发测试";
    public static final String PASSWORD = "yfcs123456";
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
         * name : null
         * successMsg : 登陆成功！
         * account : yundunProapp
         */

        private String name;
        private String successMsg;
        private String account;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSuccessMsg() {
            return successMsg;
        }

        public void setSuccessMsg(String successMsg) {
            this.successMsg = successMsg;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "name=" + name +
                    ", successMsg='" + successMsg + '\'' +
                    ", account='" + account + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SW_UserLoginBean{" +
                "retCode=" + retCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
