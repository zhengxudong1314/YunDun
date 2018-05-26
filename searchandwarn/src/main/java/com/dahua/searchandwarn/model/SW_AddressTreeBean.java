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

    public static class DataBean {


        private String orgName;
        private String orgCode;
        private List<ChildrenBeanXX> children;

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public List<ChildrenBeanXX> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBeanXX> children) {
            this.children = children;
        }

        public static class ChildrenBeanXX {


            private String orgName;
            private String orgCode;
            private List<ChildrenBeanX> children;

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getOrgCode() {
                return orgCode;
            }

            public void setOrgCode(String orgCode) {
                this.orgCode = orgCode;
            }

            public List<ChildrenBeanX> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBeanX> children) {
                this.children = children;
            }

            public static class ChildrenBeanX {

                private String orgName;
                private String orgCode;
                private String devCode;
                private String devName;
                private List<ChildrenBean> children;

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

                public String getOrgName() {
                    return orgName;
                }

                public void setOrgName(String orgName) {
                    this.orgName = orgName;
                }

                public String getOrgCode() {
                    return orgCode;
                }

                public void setOrgCode(String orgCode) {
                    this.orgCode = orgCode;
                }

                public List<ChildrenBean> getChildren() {
                    return children;
                }

                public void setChildren(List<ChildrenBean> children) {
                    this.children = children;
                }

                public static class ChildrenBean {
                    /**
                     * devCode : 5003187328781728174
                     * org_id : 1001212
                     * devY : 29.8137682617
                     * devName : 复兴所控单元门市马路对面墙壁
                     * devX : 106.5599933267
                     */

                    private String devCode;
                    private String org_id;
                    private double devY;
                    private String devName;
                    private double devX;
                    private String cbStr;

                    public String getCbStr() {
                        return cbStr;
                    }

                    public void setCbStr(String cbStr) {
                        this.cbStr = cbStr;
                    }

                    public String getDevCode() {
                        return devCode;
                    }

                    public void setDevCode(String devCode) {
                        this.devCode = devCode;
                    }

                    public String getOrg_id() {
                        return org_id;
                    }

                    public void setOrg_id(String org_id) {
                        this.org_id = org_id;
                    }

                    public double getDevY() {
                        return devY;
                    }

                    public void setDevY(double devY) {
                        this.devY = devY;
                    }

                    public String getDevName() {
                        return devName;
                    }

                    public void setDevName(String devName) {
                        this.devName = devName;
                    }

                    public double getDevX() {
                        return devX;
                    }

                    public void setDevX(double devX) {
                        this.devX = devX;
                    }
                }
            }
        }
    }
}
