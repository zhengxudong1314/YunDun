package com.dahua.searchandwarn.modules.facesearching.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_StaticBean;

public class SW_StaticDialogActivity extends AppCompatActivity {

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvSimilarity;
    private TextView tvKuName;
    private TextView tvKuId;
    private TextView tvCardId;
    private TextView tvTitle;
    private ImageView ivBack;
    private TextView tvPersonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_dialog_static);
        initView();
        SW_StaticBean.DataBean datas = (SW_StaticBean.DataBean) getIntent().getSerializableExtra("datas");

        Glide.with(this).load("").into(ivImg);
        tvTitle.setText("静态比对");
        tvSimilarity.setText(datas.getSimilarity() + "%");
        tvName.setText(datas.getName());
        tvCardId.setText(datas.getCardNum());
        tvKuId.setText(datas.getLibId());
        tvKuName.setText(datas.getLibName());
        tvPersonId.setText(datas.getPeopleId());
        Glide.with(this).load(datas.getFace()).placeholder(R.drawable.sw_icon_img_unselected).into(ivImg);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        ivImg = (ImageView) findViewById(R.id.iv_img);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_name);
        tvName = (TextView) findViewById(R.id.tv_title);
        tvSimilarity = (TextView) findViewById(R.id.tv_similarity);
        tvKuName = (TextView) findViewById(R.id.tv_kuname);
        tvKuId = (TextView) findViewById(R.id.tv_kuid);
        tvCardId = (TextView) findViewById(R.id.tv_cardId);
        tvPersonId = (TextView) findViewById(R.id.tv_personid);

    }
}
