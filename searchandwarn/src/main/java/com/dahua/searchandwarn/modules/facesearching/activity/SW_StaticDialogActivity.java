package com.dahua.searchandwarn.modules.facesearching.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_StaticBean;
import com.dahua.searchandwarn.utils.TwoPointUtils;

public class SW_StaticDialogActivity extends AppCompatActivity {

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvSimilarity;
    private TextView tvKuName;
    private TextView tvCardId;
    private TextView tvTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_dialog_static);
        initView();
        SW_StaticBean.DataBean datas = (SW_StaticBean.DataBean) getIntent().getSerializableExtra("datas");

        tvTitle.setText("静态比对");
        tvSimilarity.setText(TwoPointUtils.doubleToString(Double.valueOf(datas.getSimilarity())) + "%");
        tvName.setText(datas.getName());
        tvCardId.setText(datas.getCardNum());
        tvKuName.setText(datas.getLibName());
        Glide.with(this).load(datas.getFace()).placeholder(R.drawable.sw_home_page_defect).into(ivImg);
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
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvSimilarity = (TextView) findViewById(R.id.tv_similarity);
        tvKuName = (TextView) findViewById(R.id.tv_kuname);
        tvCardId = (TextView) findViewById(R.id.tv_cardId);

    }

}
