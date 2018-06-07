package com.dahua.searchandwarn.modules.warning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.base.LoadingDialogUtils;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_IgnoreBean;
import com.dahua.searchandwarn.model.SW_SingleWarnBean;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.KeyboardUtils;
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

public class SW_IgnoreActivity extends AppCompatActivity implements View.OnClickListener {
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
    private TextView tvCacle;
    private TextView tvSure;
    private Intent intent;
    private CompositeDisposable compositeDisposable;
    private String alarmId;
    private String operator;
    private EditText etReason;
    private String ignoreMsg;
    private SW_SingleWarnBean.DataBean datas;
    private TextView tvLoadingError;
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_ignore);
        initView();
        getBaseNetData();
        tvTitle.setText(getString(R.string.ignore_warn));
    }

    private void getBaseNetData() {
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

                            tvLoadingError.setVisibility(View.GONE);
                            LoadingDialogUtils.dismiss();
                            datas = sw_singleWarnBean.getData();
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
                            Glide.with(SW_IgnoreActivity.this).load(datas.getOriginalImg()).placeholder(R.drawable.sw_home_page_defect).into(ivTwo);
                            Glide.with(SW_IgnoreActivity.this).load(datas.getSmallImg()).placeholder(R.drawable.sw_home_page_defect).into(ivOne);
                            Glide.with(SW_IgnoreActivity.this).load(datas.getBigImg()).placeholder(R.drawable.sw_home_page_defect).into(ivThree);
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

    private void getNetData() {
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_IgnoreBean>>() {
                    @Override
                    public ObservableSource<SW_IgnoreBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.warnIgnore(alarmId, ignoreMsg, operator);
                        } else {
                            return null;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_IgnoreBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_IgnoreBean sw_ignoreBean) {
                        int retCode = sw_ignoreBean.getRetCode();
                        if (retCode == 0) {
                            EventBus.getDefault().postSticky(position);
                            ToastUtils.showShort("忽略成功");
                            LoadingDialogUtils.dismiss();
                            finish();
                        } else {
                            ToastUtils.showLong(sw_ignoreBean.getMessage());
                            EventBus.getDefault().postSticky("false");
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("忽略失败");
                    }

                    @Override
                    public void onComplete() {
                        LoadingDialogUtils.dismiss();

                    }
                });

    }


    private void initView() {

        alarmId = getIntent().getStringExtra("alarmId");
        position = getIntent().getStringExtra("position");
        operator = getIntent().getStringExtra("operator");
        intent = new Intent(SW_IgnoreActivity.this, SW_PhotoActivity.class);
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
        tvCacle = (TextView) findViewById(R.id.tv_cacle);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        etReason = (EditText) findViewById(R.id.et_reason);
        tvLoadingError = (TextView) findViewById(R.id.tv_loading_error);
        //点击事件
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivOne.setOnClickListener(this);
        ivTwo.setOnClickListener(this);
        ivThree.setOnClickListener(this);
        tvCacle.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        LoadingDialogUtils.init(this);
        LoadingDialogUtils.show(this);
    }

    @Override
    public void onBackPressed() {
        LoadingDialogUtils.dismiss();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.iv_back) {
            KeyboardUtils.hideSoftInput(SW_IgnoreActivity.this);
            finish();
        } else if (i == R.id.iv_search) {
            //跳转查询页面
            startActivity(new Intent(SW_IgnoreActivity.this.getApplicationContext(), SW_SearchActivity.class));
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
        } else if (i == R.id.tv_cacle) {
            KeyboardUtils.hideSoftInput(SW_IgnoreActivity.this);
            finish();
        } else if (i == R.id.tv_sure) {
            ignoreMsg = etReason.getText().toString().trim();
            LoadingDialogUtils.show(this);
            getNetData();

        }
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
