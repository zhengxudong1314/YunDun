package com.dahua.searchandwarn.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_AddressTreeBean;
import com.dahua.searchandwarn.model.SW_DeviceCodeBean;
import com.dahua.searchandwarn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/16
 * 功能：
 */

public class SW_AddressTreeAdapter extends BaseQuickAdapter<SW_AddressTreeBean.DataBean, BaseViewHolder> {

    private Activity activity;
    private String address;

    public SW_AddressTreeAdapter(Activity activity, int layoutResId, @Nullable List<SW_AddressTreeBean.DataBean> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final SW_AddressTreeBean.DataBean item) {
        helper.setText(R.id.cb_name, item.getOrgName());
        final CheckBox cb_name = helper.getView(R.id.cb_name);
        final RecyclerView rv = helper.getView(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(activity));
        rv.setAdapter(new BaseQuickAdapter<SW_AddressTreeBean.DataBean.ChildrenBeanXX, BaseViewHolder>(R.layout.sw_view_textview, item.getChildren()) {

            @Override
            protected void convert(BaseViewHolder helper, final SW_AddressTreeBean.DataBean.ChildrenBeanXX item1) {
                final CheckBox cb_name1 = helper.getView(R.id.cb_name);
                cb_name1.setText(item1.getOrgName());
                final RecyclerView rv1 = helper.getView(R.id.rv);
                ImageView iv_log = helper.getView(R.id.iv_log);
                if (TextUtils.isEmpty(item1.getOrgName())){
                    helper.setText(R.id.cb_name, item1.getDevName());
                    iv_log.setVisibility(View.INVISIBLE);
                    cb_name1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            address = item.getOrgName()+item1.getDevName();
                            EventBus.getDefault().post(new SW_DeviceCodeBean(item1.getDevCode(), address));
                            activity.finish();
                        }
                    });
                }else {
                    cb_name1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean checked = cb_name1.isChecked();
                            if (checked) {
                                rv1.setVisibility(View.VISIBLE);
                            } else {
                                rv1.setVisibility(View.GONE);
                            }


                        }
                    });
                }
                rv1.setLayoutManager(new LinearLayoutManager(activity));
                rv1.setAdapter(new BaseQuickAdapter<SW_AddressTreeBean.DataBean.ChildrenBeanXX.ChildrenBeanX, BaseViewHolder>(R.layout.sw_view_textview, item1.getChildren()) {

                    @Override
                    protected void convert(BaseViewHolder helper, final SW_AddressTreeBean.DataBean.ChildrenBeanXX.ChildrenBeanX item2) {
                        ImageView iv_log = helper.getView(R.id.iv_log);
                        helper.setText(R.id.cb_name, item2.getOrgName());
                        final CheckBox cb_name2 = helper.getView(R.id.cb_name);
                        final RecyclerView rv2 = helper.getView(R.id.rv);
                        if (TextUtils.isEmpty(item2.getOrgName())){
                            helper.setText(R.id.cb_name, item2.getDevName());
                            iv_log.setVisibility(View.INVISIBLE);
                            cb_name2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    address = item.getOrgName()+item1.getOrgName()+item2.getDevName();
                                    EventBus.getDefault().post(new SW_DeviceCodeBean(item2.getDevCode(), address));
                                    activity.finish();
                                }
                            });
                        }else {
                            cb_name2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    boolean checked = cb_name2.isChecked();
                                    if (checked) {
                                        rv2.setVisibility(View.VISIBLE);
                                    } else {
                                        rv2.setVisibility(View.GONE);
                                    }


                                }
                            });
                        }

                        rv2.setLayoutManager(new LinearLayoutManager(activity));
                        rv2.setAdapter(new BaseQuickAdapter<SW_AddressTreeBean.DataBean.ChildrenBeanXX.ChildrenBeanX.ChildrenBean, BaseViewHolder>(R.layout.sw_view_textview_item, item2.getChildren()) {

                            @Override
                            protected void convert(BaseViewHolder helper, final SW_AddressTreeBean.DataBean.ChildrenBeanXX.ChildrenBeanX.ChildrenBean item3) {

                                helper.setText(R.id.tv_name, item3.getDevName());
                                TextView tv_name = helper.getView(R.id.tv_name);
                                tv_name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        address = item.getOrgName()+item1.getOrgName()+item2.getOrgName()+item3.getDevName();
                                        EventBus.getDefault().post(new SW_DeviceCodeBean(item3.getDevCode(), address));
                                        activity.finish();
                                    }
                                });

                            }
                        });

                    }
                });

            }
        });

        cb_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = cb_name.isChecked();
                if (checked) {
                    rv.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.GONE);
                }
            }
        });

    }
}
