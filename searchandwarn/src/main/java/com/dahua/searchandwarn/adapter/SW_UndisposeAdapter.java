package com.dahua.searchandwarn.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.base.SqlietModel;
import com.dahua.searchandwarn.model.SW_HistoryWarnBean;
import com.dahua.searchandwarn.model.SW_UnReadNum;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.modules.warning.activity.SW_DisposeDetailsActivity;
import com.dahua.searchandwarn.modules.warning.activity.SW_IgnoreActivity;
import com.dahua.searchandwarn.modules.warning.activity.SW_WarningDetailsActivity;
import com.dahua.searchandwarn.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/10
 * 功能：
 */

public class SW_UndisposeAdapter extends BaseQuickAdapter<SW_HistoryWarnBean.DataBean, BaseViewHolder> {
    private Context context;
    private List<String> list;
    private Intent intent;
    private SqlietModel sqlietModel;
    private int num ;
    public SW_UndisposeAdapter(Context context, int layoutResId, @Nullable List<SW_HistoryWarnBean.DataBean> data, List<String> list) {
        super(layoutResId, data);
        EventBus.getDefault().register(this);
        this.context = context;
        this.list = list;
        sqlietModel = new SqlietModel(context);

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMood(SW_UnReadNum num){
        this.num = num.getNum();
        LogUtils.e(""+num.getNum());
    }

    @Override
    protected void convert(BaseViewHolder helper, final SW_HistoryWarnBean.DataBean item) {

        final ImageView iv_type = helper.getView(R.id.iv_type);
        int flag = 0;
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(item.getAlarmId())) {
                    iv_type.setImageResource(R.drawable.sw_icon_read);
                    flag = 0;
                    break;
                } else {
                    flag = 1;
                }
            }
            if (flag == 1) {
                iv_type.setImageResource(R.drawable.sw_icon_unread);
            }
        } else {
            iv_type.setImageResource(R.drawable.sw_icon_unread);
        }
        final int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_time, item.getSaveTime())
                .setText(R.id.tv_site, item.getDeviceName())
                .setText(R.id.tv_id, item.getFaceName())
                .addOnClickListener(R.id.tv_ignore)
                .addOnClickListener(R.id.tv_dispose)
                .addOnClickListener(R.id.tv_details)
                .addOnClickListener(R.id.tv_location);
        RelativeLayout rl_bg = helper.getView(R.id.rl_bg);

        LinearLayout tv_ignore = helper.getView(R.id.tv_ignore);
        LinearLayout tv_dispose = helper.getView(R.id.tv_dispose);
        LinearLayout tv_details = helper.getView(R.id.tv_details);
        LinearLayout tv_location = helper.getView(R.id.tv_location);


        tv_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转忽略页面
                iv_type.setImageResource(R.drawable.sw_icon_read);
                list = sqlietModel.queryAll();
                numdown(item);
                sqlietModel.insertData(item.getAlarmId());
                list = sqlietModel.queryAll();
                intent = new Intent(context, SW_IgnoreActivity.class);
                intent.putExtra("alarmId", item.getAlarmId());
                intent.putExtra("operator", SW_UserLoginBean.USERNANE);
                intent.putExtra("position", position+"");
                context.startActivity(intent);
                notifyDataSetChanged();

            }
        });
        tv_dispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转处理页面
                iv_type.setImageResource(R.drawable.sw_icon_read);

                list = sqlietModel.queryAll();
                numdown(item);
                sqlietModel.insertData(item.getAlarmId());
                list = sqlietModel.queryAll();
                intent = new Intent(context, SW_DisposeDetailsActivity.class);
                intent.putExtra("alarmId", item.getAlarmId());
                intent.putExtra("operator", SW_UserLoginBean.USERNANE);
                intent.putExtra("position", position+"");
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        tv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转详情页面
                iv_type.setImageResource(R.drawable.sw_icon_read);
                list = sqlietModel.queryAll();
                numdown(item);
                sqlietModel.insertData(item.getAlarmId());
                list = sqlietModel.queryAll();
                intent = new Intent(context, SW_WarningDetailsActivity.class);
                intent.putExtra("alarmId", item.getAlarmId());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转定位页面
                iv_type.setImageResource(R.drawable.sw_icon_read);
                list = sqlietModel.queryAll();
                numdown(item);
                sqlietModel.insertData(item.getAlarmId());
                list = sqlietModel.queryAll();
                intent = new Intent();
                intent.setComponent(new ComponentName("com.mm.dss.map", "com.mm.dss.map.BaiduMapActivity"));
                intent.putExtra("longitude", 116.222);//经度
                intent.putExtra("latitued", 83.222);//纬度
                intent.putExtra("name", "dahua");
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });


        if (position % 2 == 0) {
            rl_bg.setBackgroundColor(context.getResources().getColor(R.color.tvLightBlue));
        } else {
            rl_bg.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }
    }

    private void numdown(SW_HistoryWarnBean.DataBean item) {
        int flag = 0;
        if (list.size()==0){
            num--;
            EventBus.getDefault().postSticky(new SW_UnReadNum(num));
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(item.getAlarmId())){
                flag = 0;
                break;
            }else {
                flag = 1;
            }
        }
        if (flag==1){
            num--;
            EventBus.getDefault().postSticky(new SW_UnReadNum(num));
        }
    }

}
