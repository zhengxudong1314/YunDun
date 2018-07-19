package com.dahua.searchandwarn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_HistoryWarnBean;
import com.dahua.searchandwarn.modules.warning.activity.SW_WarningDetailsActivity;

import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/9
 * 功能：
 */

public class SW_DisposeAdapter extends BaseQuickAdapter<SW_HistoryWarnBean.DataBean, BaseViewHolder> {
    private Context context;
    private SW_HistoryWarnBean.DataBean bean;

    public SW_DisposeAdapter(Context context, int layoutResId, @Nullable List<SW_HistoryWarnBean.DataBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final SW_HistoryWarnBean.DataBean item) {
        this.bean = item;
        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_time, item.getSaveTime())
                .setText(R.id.tv_site, item.getDeviceName())
                .setText(R.id.tv_id, item.getFaceName());
        LinearLayout tvDetails = helper.getView(R.id.tv_details);
        LinearLayout tvLocation = helper.getView(R.id.tv_location);
        tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SW_WarningDetailsActivity.class);
                //intent = new Intent(context, SW_WarningDetailsActivity.class);
                intent.putExtra("alarmId", item.getAlarmId());
                context.startActivity(intent);
            }
        });
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //intent.setComponent(new ComponentName("com.mm.dss", "com.mm.dss.map.BaiduMapActivity"));
                intent.setAction("com.mm.dss.alarm.map");
                intent.putExtra("longtitude", bean.getDeviceX());//经度
                intent.putExtra("latitude", bean.getDeviceY());//纬度
                intent.putExtra("name", "dahua");
                context.startActivity(intent);
            }
        });
        LinearLayout ll_bg = helper.getView(R.id.ll_bg);
        if (position%2==0){
            ll_bg.setBackgroundColor(context.getResources().getColor(R.color.tvLightBlue));
        }else {
            ll_bg.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }
    }


}
