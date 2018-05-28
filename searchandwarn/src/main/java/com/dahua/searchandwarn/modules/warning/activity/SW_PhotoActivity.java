package com.dahua.searchandwarn.modules.warning.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;

public class SW_PhotoActivity extends AppCompatActivity {

    private PhotoView photoView;
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_photo);
        photoView = (PhotoView) findViewById(R.id.photoView);
        tvBack = (TextView) findViewById(R.id.tv_back);
        String imgUrl = getIntent().getStringExtra("imgUrl");
        // 启用图片缩放功能
        photoView.enable();
        Glide.with(this).load(imgUrl).placeholder(R.drawable.sw_icon_img_unselected).into(photoView);

        // 获取/设置 最大缩放倍数
        photoView.setMaxScale(10);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
