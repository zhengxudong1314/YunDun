package com.dahua.searchandwarn.model;

import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/16
 * 功能：
 */

public class SW_AddressTreeBean {
    private int retCode;
    private String message;
    private List<BaseInfo> data;

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

    public List<BaseInfo> getData() {
        return data;
    }

    public void setData(List<BaseInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SW_AddressTreeBean{" +
                "retCode=" + retCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class BaseInfo {
        private String orgCode;
        private String orgName;
        private String devCode;
        private String devName;
        private String org_id;
        private List<BaseInfo> children;
        private boolean isExpended = false;
        private int level = 0;
        private boolean isEmptyNode = false;
        private boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isEmptyNode() {
            return isEmptyNode;
        }

        public void setEmptyNode(boolean emptyNode) {
            isEmptyNode = emptyNode;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getDevCode() {
            return devCode;
        }

        public void setDevCode(String devCode) {
            this.devCode = devCode;
        }

        public String getDevName() {
            return devName;
        }

        public void setDevName(String devName) {
            this.devName = devName;
        }

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public List<BaseInfo> getChildren() {
            return children;
        }

        public void setChildren(List<BaseInfo> children) {
            this.children = children;
        }

        public boolean isExpended() {
            return isExpended;
        }

        public void setExpended(boolean expended) {
            isExpended = expended;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return "BaseInfo{" +
                    "orgCode='" + orgCode + '\'' +
                    ", orgName='" + orgName + '\'' +
                    ", devCode='" + devCode + '\'' +
                    ", devName='" + devName + '\'' +
                    ", org_id='" + org_id + '\'' +
                    ", children=" + children +
                    ", isExpended=" + isExpended +
                    ", level=" + level +
                    '}';
        }
    }
}
