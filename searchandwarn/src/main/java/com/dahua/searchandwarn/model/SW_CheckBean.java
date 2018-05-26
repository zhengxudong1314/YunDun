package com.dahua.searchandwarn.model;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/19
 */

public class SW_CheckBean {
    private boolean isChecked;
    private int position;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
