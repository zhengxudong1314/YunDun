package com.dahua.searchandwarn.modules.warning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.adapter.SW_HistoryWarnAdapter;
import com.dahua.searchandwarn.base.LoadingDialogUtils;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_HistoryWarnBean;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SW_HistroyWarnActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private RecyclerView rv;
    private TextView tvNoData;
    private SW_HistoryWarnAdapter historyWarnAdapter;
    private List<SW_HistoryWarnBean.DataBean> datas;
    private Intent intent;
    private CompositeDisposable compositeDisposable;
    private TextView tvLoadingError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_history_warn);
        initView();

        getNetData(getIntentData());
        tvTitle.setText("预警历史");
        intent = new Intent(SW_HistroyWarnActivity.this, SW_WarningDetailsActivity.class);


    }

    @NonNull
    private Map<String, String> getIntentData() {
        Intent intent = getIntent();
        String cardId = intent.getStringExtra("cardId");
        String reason = intent.getStringExtra("reason");
        String startTime = intent.getStringExtra("startTime");
        String endTime = intent.getStringExtra("endTime");
        String site = intent.getStringExtra("site");
        String state = intent.getStringExtra("state");
        if (state == null) {
            state = "1,2,3,4";
        }
        Map<String, String> map = new HashMap<>();
        //如果条件为默认值，则为null
        if (!startTime.equals(getString(R.string.choose))) {
            map.put("startTime", startTime);
        }

        if (!endTime.equals(getString(R.string.choose))) {
            map.put("endTime", endTime);
        }

        if (site != null && !site.equals(getString(R.string.choose))) {
            map.put("deviceCodes", site);
        }

        if (!state.equals(getString(R.string.choose))) {
            map.put("status", state);
        }
        if (!TextUtils.isEmpty(reason)) {
        }
        if (!TextUtils.isEmpty(cardId)) {
            map.put("reason", reason);
        }
        // todo  替换用户名为 SW_UserLoginBean.USERNANE
        map.put("appUser", SW_UserLoginBean.USERNANE);
        map.put("pageNum", "1");
        map.put("pageSize", "50");
        return map;
    }

    private void getNetData(final Map<String, String> map) {
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_HistoryWarnBean>>() {
                    @Override
                    public ObservableSource<SW_HistoryWarnBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.getHistoryWarn(map);
                        } else {
                            return null;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_HistoryWarnBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_HistoryWarnBean historyWarnBean) {
                        int retCode = historyWarnBean.getRetCode();
                        if (retCode == 0) {
                            LoadingDialogUtils.dismiss();
                            datas = historyWarnBean.getData();
                            tvLoadingError.setVisibility(View.GONE);
                            if (datas == null || datas.size() == 0) {
                                tvNoData.setVisibility(View.VISIBLE);
                                tvLoadingError.setVisibility(View.GONE);
                            } else {
                                rv.setLayoutManager(new LinearLayoutManager(SW_HistroyWarnActivity.this));
                                historyWarnAdapter = new SW_HistoryWarnAdapter(R.layout.sw_item_history_warn, datas);
                                rv.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);
                                rv.setAdapter(historyWarnAdapter);
                                historyWarnAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        intent.putExtra("alarmId", datas.get(position).getAlarmId());
                                        startActivity(SW_HistroyWarnActivity.this.intent);
                                    }
                                });
                            }
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
        tvLoadingError = (TextView) findViewById(R.id.tv_loading_error);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rv = (RecyclerView) findViewById(R.id.rv);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        ivBack.setOnClickListener(this);
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingDialogUtils.unInit();
        compositeDisposable.dispose();
    }
}
