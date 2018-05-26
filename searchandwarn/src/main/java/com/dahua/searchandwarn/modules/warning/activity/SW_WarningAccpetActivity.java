package com.dahua.searchandwarn.modules.warning.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.base.LoadingDialogUtils;
import com.dahua.searchandwarn.modules.warning.fragment.SW_DisposeFragment;
import com.dahua.searchandwarn.modules.warning.fragment.SW_UndisposeFragment;
import com.dahua.searchandwarn.utils.Utils;
import com.dahua.searchandwarn.weight.SW_NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SW_WarningAccpetActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private ImageView ivSearch;
    private TextView tvTitle;
    private TextView tvLoadingError;
    private SW_NoScrollViewPager viewPager;
    private TabLayout tabLayout;
    private List<String> tabs;
    private List<Fragment> listFragments;
    private View myLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        /*if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }*/
        setContentView(R.layout.sw_activity_warning_accpet);
        Utils.init(this.getApplication());
        EventBus.getDefault().register(this);
        //初始化控件
        initView();
        initData();
        viewPager.setNoScroll(true);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabs.get(position);
            }

            @Override
            public Fragment getItem(int position) {
                return listFragments.get(position);
            }

            @Override
            public int getCount() {
                return listFragments.size();
            }
        });
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        //设置tab下划线长度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 50, 50);
            }
        });
    }


    private void initData() {
        myLine.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.warn_accpet));
        ivSearch.setImageResource(R.drawable.sw_icon_search);
        tabs = new ArrayList<>();
        listFragments = new ArrayList<>();
        tabs.add("处理中");
        tabs.add("已处理");
        listFragments.add(new SW_UndisposeFragment());
        listFragments.add(new SW_DisposeFragment());
    }

    private void initView() {
        tvLoadingError = (TextView) findViewById(R.id.tv_loading_error);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        viewPager = (SW_NoScrollViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        myLine = findViewById(R.id.my_line);
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        LoadingDialogUtils.init(this);
        LoadingDialogUtils.show(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.iv_search) {
            startActivity(new Intent(SW_WarningAccpetActivity.this.getApplicationContext(), SW_SearchActivity.class));
        }
    }

    //设置tab下划线长度
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMoon(String str) {
        if (str.equals("failure")){
            tvLoadingError.setText("加载失败");
            tvLoadingError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LoadingDialogUtils.unInit();
    }


}
