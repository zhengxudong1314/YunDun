package com.dahua.searchandwarn.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_SingleWarnBean;

import java.util.List;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/19
 */

public class SW_LogAdapter extends BaseQuickAdapter<SW_SingleWarnBean.DataBean.LoglistBean,BaseViewHolder> {
    public SW_LogAdapter(int layoutResId, @Nullable List<SW_SingleWarnBean.DataBean.LoglistBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SW_SingleWarnBean.DataBean.LoglistBean item) {
        helper.setText(R.id.tv_time, item.getRecordTime())
                .setText(R.id.tv_person, item.getRecordPeople())
                .setText(R.id.tv_message, item.getRecordMessage());
    }
}
