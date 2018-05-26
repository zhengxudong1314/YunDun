package com.dahua.searchandwarn.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_HistoryWarnBean;

import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/14
 * 功能：
 */

public class SW_HistoryWarnAdapter extends BaseQuickAdapter<SW_HistoryWarnBean.DataBean,BaseViewHolder> {
    public SW_HistoryWarnAdapter(int layoutResId, @Nullable List<SW_HistoryWarnBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SW_HistoryWarnBean.DataBean item) {
        helper.setText(R.id.tv_time, item.getSaveTime())
                .setText(R.id.tv_id,item.getFaceName())
                .setText(R.id.tv_site,item.getDeviceName());
    }


}
