package com.dahua.searchandwarn.modules.facesearching.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_DynamicBean;
import com.dahua.searchandwarn.utils.TwoPointUtils;

public class SW_DynamicDialogActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView ivBack;
    private SW_DynamicBean.DataBean dataBean;
    private TextView tvSimilarity;
    private TextView tvTime;
    private TextView tvAge;
    private TextView tvSex;
    private TextView tvsite;
    private ImageView ivSmall;
    private ImageView ivBig;
    private TextView tvName;
    private TextView tvCardId;
    private TextView tvHuji;
    private TextView tvJiguan;
    private TextView tvChangshang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_dynamic_dialog);
        initView();
        dataBean = (SW_DynamicBean.DataBean) getIntent().getSerializableExtra("datas");
        tvTitle.setText("动态对比");
        tvSimilarity.setText(TwoPointUtils.doubleToString(Double.valueOf(dataBean.getSimilarity())) + "%");
        tvTime.setText(dataBean.getFaceTime());
        if (dataBean.getAge() > 0) {
            tvAge.setText(dataBean.getAge() + "");
        }
        String sex = dataBean.getSex();
        if (sex.equals("0")) {
            tvSex.setText("男");
        } else {
            tvSex.setText("女");
        }
        tvsite.setText(dataBean.getDevName());
        if (dataBean.getName() != null) {
            tvName.setText(dataBean.getName());
        }
        if (dataBean.getIdCardCode() != null) {
            tvCardId.setText(dataBean.getIdCardCode());
        }
        if (dataBean.getOriginPlace() != null) {
            tvJiguan.setText(dataBean.getOriginPlace());
        }
        if (dataBean.getHousehold() != null) {
            tvHuji.setText(dataBean.getHousehold());
        }
        if (dataBean.getFactoryName() != null) {
            tvChangshang.setText(dataBean.getFactoryName());
        }
        Glide.with(this).load(dataBean.getSource_image1()).placeholder(R.drawable.sw_home_page_defect).into(ivSmall);
        Glide.with(this).load(dataBean.getSource_image2()).placeholder(R.drawable.sw_home_page_defect).into(ivBig);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvsite = (TextView) findViewById(R.id.tv_site);
        tvSimilarity = (TextView) findViewById(R.id.tv_similarity);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCardId = (TextView) findViewById(R.id.tv_cardId);
        tvHuji = (TextView) findViewById(R.id.tv_huji);
        tvJiguan = (TextView) findViewById(R.id.tv_jiguan);
        tvChangshang = (TextView) findViewById(R.id.tv_changshang);
        ivSmall = (ImageView) findViewById(R.id.iv_small);
        ivBig = (ImageView) findViewById(R.id.iv_big);

    }
}
