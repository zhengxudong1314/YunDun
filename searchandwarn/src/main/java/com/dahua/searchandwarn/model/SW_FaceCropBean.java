package com.dahua.searchandwarn.model;

import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/15
 * 功能：
 */

public class SW_FaceCropBean {

    /**
     * retCode : 0
     * message : 人脸裁剪成功
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

    public static class DataBean {
        /**
         * obj_right : 460
         * face_gender : 0
         * obj_bottom : 305
         * obj_left : 332
         * obj_top : 177
         * face_age : 21
         * whole_height : 1080
         * whole_width : 1920
         * smallImgBase64 :
         */

        private int obj_right;
        private int face_gender;
        private int obj_bottom;
        private int obj_left;
        private int obj_top;
        private int face_age;
        private int whole_height;
        private int whole_width;
        private String smallImgBase64;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getObj_right() {
            return obj_right;
        }

        public void setObj_right(int obj_right) {
            this.obj_right = obj_right;
        }

        public int getFace_gender() {
            return face_gender;
        }

        public void setFace_gender(int face_gender) {
            this.face_gender = face_gender;
        }

        public int getObj_bottom() {
            return obj_bottom;
        }

        public void setObj_bottom(int obj_bottom) {
            this.obj_bottom = obj_bottom;
        }

        public int getObj_left() {
            return obj_left;
        }

        public void setObj_left(int obj_left) {
            this.obj_left = obj_left;
        }

        public int getObj_top() {
            return obj_top;
        }

        public void setObj_top(int obj_top) {
            this.obj_top = obj_top;
        }

        public int getFace_age() {
            return face_age;
        }

        public void setFace_age(int face_age) {
            this.face_age = face_age;
        }

        public int getWhole_height() {
            return whole_height;
        }

        public void setWhole_height(int whole_height) {
            this.whole_height = whole_height;
        }

        public int getWhole_width() {
            return whole_width;
        }

        public void setWhole_width(int whole_width) {
            this.whole_width = whole_width;
        }

        public String getSmallImgBase64() {
            return smallImgBase64;
        }

        public void setSmallImgBase64(String smallImgBase64) {
            this.smallImgBase64 = smallImgBase64;
        }
    }
}
