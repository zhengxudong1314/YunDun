package com.dahua.searchandwarn.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_AddressTreeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建： ZXD
 * 日期 2018/5/16
 * 功能：
 */

public class SW_AddressTreeAdapter extends BaseQuickAdapter<SW_AddressTreeBean.BaseInfo, BaseViewHolder> {

    private List<SW_AddressTreeBean.BaseInfo> baseInfos;
    private Context context;
    private Map<String, SW_AddressTreeBean.BaseInfo> map = new HashMap<>();
    private int mItemHeight;
    private boolean onBind;

    public SW_AddressTreeAdapter(Context context, int layoutResId, @Nullable List<SW_AddressTreeBean.BaseInfo> data) {
        super(layoutResId, data);
        this.baseInfos = data;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final SW_AddressTreeBean.BaseInfo item) {
        onBind = true;
        //filter(item);
        final int position = helper.getAdapterPosition();
        View rootView = helper.getView(R.id.ll);
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        if (params.height != 0) {
            mItemHeight = params.height;
        }
        params.height = item.isEmptyNode() ? 0 : mItemHeight;
        rootView.setLayoutParams(params);
        rootView.setVisibility(item.isEmptyNode() ? View.GONE : View.VISIBLE);

        ImageView iv = helper.getView(R.id.iv);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(item.getLevel() * 40, 0, 0, 0);
        iv.setLayoutParams(layoutParams);
        if (TextUtils.isEmpty(item.getDevCode())) {
            iv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(item.getOrgName())) {
                helper.setText(R.id.tv_name, item.getOrgName());
            } else {
                helper.setText(R.id.tv_name, "");
            }
        } else {
            iv.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(item.getDevName())) {
                helper.setText(R.id.tv_name, item.getDevName());
            } else {
                helper.setText(R.id.tv_name, "");
            }
        }

        helper.setChecked(R.id.cb, item.isChecked());
        final CheckBox cb = helper.getView(R.id.cb);
        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.isExpended()) {
                    item.setExpended(false);
                    SW_AddressTreeBean.BaseInfo baseInfo = map.get(item.getOrgCode());
                    int start = position + 1;
                    int end = baseInfos.indexOf(baseInfo) + 1;
                    List<SW_AddressTreeBean.BaseInfo> delete = new ArrayList<>();
                    for (int i = start; i < end; i++) {
                        delete.add(baseInfos.get(i));
                    }

                    baseInfos.removeAll(delete);
                    map.remove(item.getOrgCode());
                } else {
                    item.setExpended(true);

                    if (item.getChildren() != null) {
                        for (int i = 0; i < item.getChildren().size(); i++) {
                            item.getChildren().get(i).setLevel(item.getLevel() + 1);
                        }
                        baseInfos.addAll(position + 1, item.getChildren());
                        SW_AddressTreeBean.BaseInfo ba = new SW_AddressTreeBean.BaseInfo();
                        ba.setEmptyNode(true);
                        map.put(item.getOrgCode(), ba);
                        baseInfos.add(position + 1 + item.getChildren().size(), ba);

                    }

                }
                notifyDataSetChanged();
            }
        });


        onBind = false;

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!onBind) {
                    item.setChecked(b);
                    isChecked(b, item);
                    notifyDataSetChanged();
                }

            }
        });
    }

    private void filter(SW_AddressTreeBean.BaseInfo item) {

        if (item.getChildren()!=null){
            for (int i = 0; i < item.getChildren().size(); i++) {
                if (item.getChildren().get(i).isChecked()){
                    item.setChecked(true);
                }else {
                    item.setChecked(false);
                    break;
                }
                filter(item.getChildren().get(i));
            }
        }
    }

    private void isChecked(boolean b, SW_AddressTreeBean.BaseInfo item) {
        if (b) {
            if (item.getChildren() != null) {
                for (int i = 0; i < item.getChildren().size(); i++) {
                    item.getChildren().get(i).setChecked(true);
                    isChecked(item.getChildren().get(i).isChecked(), item.getChildren().get(i));
                }
            }
        } else {
            if (item.getChildren() != null) {
                for (int i = 0; i < item.getChildren().size(); i++) {
                    item.getChildren().get(i).setChecked(false);
                    isChecked(item.getChildren().get(i).isChecked(), item.getChildren().get(i));
                }
            }
        }

    }
}
