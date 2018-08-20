package com.dahua.searchandwarn.modules.warning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.adapter.SW_LogAdapter;
import com.dahua.searchandwarn.base.LoadingDialogUtils;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_SingleWarnBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.TwoPointUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SW_WarningDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private ImageView ivSearch;
    private ImageView ivOne;
    private ImageView ivTwo;
    private ImageView ivThree;
    private TextView tvTitle;
    private TextView tvSimilarity;
    private TextView tvCaptureTime;
    private TextView tvId;
    private TextView tvSite;
    private TextView tvPusher;
    private TextView tvPushTime;
    private Intent intent;
    private CompositeDisposable compositeDisposable;
    private String alarmId;
    private RecyclerView rv;
    private TextView tvNoData;
    private SW_SingleWarnBean.DataBean datas;
    private TextView tvLoadingError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_warning_details);
        initView();
        tvTitle.setText("预警详情");
        getNetData();

    }

    private void getNetData() {

        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.getSingleWarn(alarmId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_SingleWarnBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_SingleWarnBean sw_singleWarnBean) {
                        if (sw_singleWarnBean.getRetCode() == 0) {
                            LoadingDialogUtils.dismiss();
                            tvLoadingError.setVisibility(View.GONE);
                            datas = sw_singleWarnBean.getData();
                            tvNoData.setVisibility(View.GONE);
                            tvSimilarity.setText(TwoPointUtils.doubleToString(datas.getSimilarity()) + "%");
                            tvCaptureTime.setText(datas.getShortTime());
                            tvId.setText(datas.getFaceCardNum());
                            if (datas.getParentPusher().equals("-1")) {
                                tvPusher.setText("系统");
                            } else {
                                tvPusher.setText(datas.getParentPusher());
                            }
                            tvSite.setText(datas.getDeviceName());
                            tvPushTime.setText(datas.getSaveTime());
                            Glide.with(SW_WarningDetailsActivity.this).load(datas.getOriginalImg()).placeholder(R.drawable.sw_home_page_defect).into(ivTwo);
                            Glide.with(SW_WarningDetailsActivity.this).load(datas.getSmallImg()).placeholder(R.drawable.sw_home_page_defect).into(ivOne);
                            Glide.with(SW_WarningDetailsActivity.this).load(datas.getBigImg()).placeholder(R.drawable.sw_home_page_defect).into(ivThree);
                            rv.setHasFixedSize(true);
                            rv.setNestedScrollingEnabled(false);
                            rv.setLayoutManager(new LinearLayoutManager(SW_WarningDetailsActivity.this));
                            rv.setAdapter(new SW_LogAdapter(R.layout.sw_item_log, datas.getLoglist()));
                        } else {
                            LoadingDialogUtils.dismiss();
                            tvLoadingError.setText("加载失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialogUtils.dismiss();
                        tvLoadingError.setText("加载失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView() {
        alarmId = getIntent().getStringExtra("alarmId");
        intent = new Intent(SW_WarningDetailsActivity.this, SW_PhotoActivity.class);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivOne = (ImageView) findViewById(R.id.iv_one);
        ivTwo = (ImageView) findViewById(R.id.iv_two);
        ivThree = (ImageView) findViewById(R.id.iv_three);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCaptureTime = (TextView) findViewById(R.id.tv_capture_time);
        tvId = (TextView) findViewById(R.id.tv_id);
        tvSite = (TextView) findViewById(R.id.tv_site);
        tvPusher = (TextView) findViewById(R.id.tv_pusher);
        tvPushTime = (TextView) findViewById(R.id.tv_push_time);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        tvSimilarity = (TextView) findViewById(R.id.tv_similarity);
        rv = (RecyclerView) findViewById(R.id.rv);
        tvLoadingError = (TextView) findViewById(R.id.tv_loading_error);
        //点击事件
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivOne.setOnClickListener(this);
        ivTwo.setOnClickListener(this);
        ivThree.setOnClickListener(this);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        LoadingDialogUtils.init(this);
        LoadingDialogUtils.show(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.iv_search) {

        } else if (i == R.id.iv_one) {
            //抓拍图片
            intent.putExtra("imgUrl", datas.getSmallImg());
            startActivity(intent);
        } else if (i == R.id.iv_two) {
            //图库图片
            intent.putExtra("imgUrl", datas.getOriginalImg());
            startActivity(intent);
        } else if (i == R.id.iv_three) {
            //选择大图
            intent.putExtra("imgUrl", datas.getBigImg());
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingDialogUtils.unInit();
        compositeDisposable.dispose();
    }
}
