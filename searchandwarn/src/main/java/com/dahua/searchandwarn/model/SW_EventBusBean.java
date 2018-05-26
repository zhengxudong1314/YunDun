package com.dahua.searchandwarn.model;

import java.util.List;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/21
 */

public class SW_EventBusBean {
    public static final int UNDIPOSES = 0;
    public static final int DIPOSES = 1;
    private List<SW_HistoryWarnBean.DataBean> list;
    private int type;

    public SW_EventBusBean(List<SW_HistoryWarnBean.DataBean> list, int type) {
        this.list = list;
        this.type = type;
    }

    public List<SW_HistoryWarnBean.DataBean> getList() {
        return list;
    }

    public void setList(List<SW_HistoryWarnBean.DataBean> list) {
        this.list = list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
