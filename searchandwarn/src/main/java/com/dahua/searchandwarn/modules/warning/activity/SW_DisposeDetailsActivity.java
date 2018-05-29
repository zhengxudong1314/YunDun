package com.dahua.searchandwarn.modules.warning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.adapter.SW_LogAdapter;
import com.dahua.searchandwarn.base.LoadingDialogUtils;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_DisposeBean;
import com.dahua.searchandwarn.model.SW_SingleWarnBean;
import com.dahua.searchandwarn.model.SW_TypeBean;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.ToastUtils;
import com.dahua.searchandwarn.utils.TwoPointUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SW_DisposeDetailsActivity extends AppCompatActivity implements View.OnClickListener {
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
    private CheckBox cbCacleWarn;
    private TextView tvCacle;
    private TextView tvSure;
    private Intent intent;
    private CompositeDisposable compositeDisposable;
    private String alarmId;
    private String operator;
    private int resultType = 0;
    private EditText etReason;
    private String suggestion;
    private TextView tvNoData;
    private SW_SingleWarnBean.DataBean datas;
    private RecyclerView rv;
    private TextView tvLoadingError;
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_dispose_details);
        initView();
        getNetData();
        tvTitle.setText(getString(R.string.warn_dispose));
        cbCacleWarn.setText(getString(R.string.no_cancle));
        cbCacleWarn.setTextColor(ContextCompat.getColor(SW_DisposeDetailsActivity.this, R.color.tvRed));
        intent = new Intent(SW_DisposeDetailsActivity.this, SW_PhotoActivity.class);

    }

    private void getNetData() {

        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_SingleWarnBean>>() {
                    @Override
                    public ObservableSource<SW_SingleWarnBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.getSingleWarn(alarmId);
                        } else {
                            return null;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_SingleWarnBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_SingleWarnBean sw_singleWarnBean) {
                        if (sw_singleWarnBean.getRetCode() == 0) {
                            tvLoadingError.setVisibility(View.GONE);
                            LoadingDialogUtils.dismiss();
                            datas = sw_singleWarnBean.getData();
                            tvNoData.setVisibility(View.GONE);
                            tvSimilarity.setText(TwoPointUtils.doubleToString(datas.getSimilarity()) + "%");
                            tvCaptureTime.setText(datas.getShortTime());
                            tvId.setText(datas.getDeviceCode());
                            if (datas.getParentPusher().equals("-1")) {
                                tvPusher.setText("系统");
                            } else {
                                tvPusher.setText(datas.getParentPusher());
                            }
                            tvSite.setText(datas.getDeviceName());
                            tvPushTime.setText(datas.getSaveTime());
                            Glide.with(SW_DisposeDetailsActivity.this).load(datas.getOriginalImg()).placeholder(R.drawable.sw_home_page_defect).into(ivTwo);
                            Glide.with(SW_DisposeDetailsActivity.this).load(datas.getSmallImg()).placeholder(R.drawable.sw_home_page_defect).into(ivOne);
                            Glide.with(SW_DisposeDetailsActivity.this).load(datas.getBigImg()).placeholder(R.drawable.sw_home_page_defect).into(ivThree);
                            rv.setHasFixedSize(true);
                            rv.setNestedScrollingEnabled(false);
                            rv.setLayoutManager(new LinearLayoutManager(SW_DisposeDetailsActivity.this));
                            rv.setAdapter(new SW_LogAdapter(R.layout.sw_item_log, datas.getLoglist()));
                        }else {
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
        position = getIntent().getStringExtra("position");
        operator = getIntent().getStringExtra("operator");
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivOne = (ImageView) findViewById(R.id.iv_one);
        ivTwo = (ImageView) findViewById(R.id.iv_two);
        ivThree = (ImageView) findViewById(R.id.iv_three);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSimilarity = (TextView) findViewById(R.id.tv_similarity);
        tvCaptureTime = (TextView) findViewById(R.id.tv_capture_time);
        tvId = (TextView) findViewById(R.id.tv_id);
        tvSite = (TextView) findViewById(R.id.tv_site);
        tvPusher = (TextView) findViewById(R.id.tv_pusher);
        tvPushTime = (TextView) findViewById(R.id.tv_push_time);
        cbCacleWarn = (CheckBox) findViewById(R.id.cb_cacle_warn);
        tvCacle = (TextView) findViewById(R.id.tv_cacle);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        etReason = (EditText) findViewById(R.id.et_reason);
        rv = (RecyclerView) findViewById(R.id.rv);
        tvLoadingError = (TextView) findViewById(R.id.tv_loading_error);
        //点击事件
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivOne.setOnClickListener(this);
        ivTwo.setOnClickListener(this);
        ivThree.setOnClickListener(this);
        tvCacle.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        cbCacleWarn.setOnClickListener(this);
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
            //跳转查询页面
            startActivity(new Intent(SW_DisposeDetailsActivity.this.getApplicationContext(), SW_SearchActivity.class));
        } else if (i == R.id.iv_one) {
            //抓拍图片
            intent.putExtra("imgUrl",datas.getSmallImg());
            startActivity(intent);
        } else if (i == R.id.iv_two) {
            //图库图片
            intent.putExtra("imgUrl",datas.getOriginalImg());
            startActivity(intent);
        } else if (i == R.id.iv_three) {
            //选择大图
            intent.putExtra("imgUrl",datas.getBigImg());
            startActivity(intent);
        } else if (i == R.id.tv_cacle) {
            finish();
        } else if (i == R.id.tv_sure) {
            suggestion = etReason.getText().toString().trim();
            if (TextUtils.isEmpty(suggestion)) {
                ToastUtils.showShort("请填写忽略原因");
            } else {
                LoadingDialogUtils.show(this);
                getDisposeData();
            }
        } else if (i == R.id.cb_cacle_warn) {
            boolean checked = cbCacleWarn.isChecked();
            if (checked) {
                resultType = 1;
                cbCacleWarn.setText(getString(R.string.yes_cancle));
                cbCacleWarn.setTextColor(ContextCompat.getColor(SW_DisposeDetailsActivity.this, R.color.tvBlue));
            } else {
                resultType = 0;
                cbCacleWarn.setText(getString(R.string.no_cancle));
                cbCacleWarn.setTextColor(ContextCompat.getColor(SW_DisposeDetailsActivity.this, R.color.tvRed));
            }
        }
    }

    private void getDisposeData() {

        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_DisposeBean>>() {
                    @Override
                    public ObservableSource<SW_DisposeBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.warnDispose(alarmId, suggestion, resultType, operator);
                        } else {
                            return null;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_DisposeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_DisposeBean sw_disposeBean) {
                        if (sw_disposeBean.getRetCode() == 0) {
                            EventBus.getDefault().postSticky(new SW_TypeBean(position));
                            ToastUtils.showLong("处理成功");
                            LoadingDialogUtils.dismiss();
                            finish();
                        }else {
                            LoadingDialogUtils.dismiss();
                            ToastUtils.showLong(sw_disposeBean.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingDialogUtils.unInit();
        compositeDisposable.dispose();
    }

    //隐藏软键盘同时使EditText失去焦点
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


}
