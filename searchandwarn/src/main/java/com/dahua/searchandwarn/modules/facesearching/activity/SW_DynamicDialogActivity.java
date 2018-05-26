package com.dahua.searchandwarn.modules.facesearching.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_DynamicBean;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_dynamic_dialog);
        initView();
        dataBean = (SW_DynamicBean.DataBean) getIntent().getSerializableExtra("datas");
        tvTitle.setText("动态对比");
        tvSimilarity.setText(dataBean.getSimilarity()+"%");
        tvTime.setText(dataBean.getFaceTime());
        tvAge.setText(dataBean.getAge());
        String sex = dataBean.getSex();
        if (sex.equals("0")){
            tvSex.setText("男");
        }else {
            tvSex.setText("女");
        }
        tvsite.setText(dataBean.getDevName());

        Glide.with(this).load(dataBean.getSource_image1()).placeholder(R.drawable.sw_icon_img_unselected).into(ivSmall);
        Glide.with(this).load(dataBean.getSource_image2()).placeholder(R.drawable.sw_icon_img_unselected).into(ivBig);
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
        ivSmall = (ImageView) findViewById(R.id.iv_small);
        ivBig = (ImageView) findViewById(R.id.iv_big);

    }
}
