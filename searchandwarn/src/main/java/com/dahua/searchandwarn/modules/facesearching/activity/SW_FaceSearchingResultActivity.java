package com.dahua.searchandwarn.modules.facesearching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_DynamicBean;
import com.dahua.searchandwarn.model.SW_FaceParams;
import com.dahua.searchandwarn.model.SW_StaticBean;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class SW_FaceSearchingResultActivity extends AppCompatActivity implements View.OnClickListener {
    private CompositeDisposable compositeDisposable;
    private String similarity;
    private String startTime;
    private String endTime;
    private String deviceCodes;
    private String imageBase64;
    private String operator;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvStaticNo;
    private TextView tvDymNo;
    private ImageView ivTu;
    private ImageView ivZhou;
    private RecyclerView rvStatic;
    private RecyclerView rvDynamic;
    private List<SW_DynamicBean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_face_searching_result);
        EventBus.getDefault().register(this);
        initView();
        tvTitle.setText("人脸比对");
        rvDynamic.setLayoutManager(new LinearLayoutManager(this));
        rvStatic.setLayoutManager(new LinearLayoutManager(this));
        getStaticData();
        getDynamicData();
    }

    private void initView() {
        compositeDisposable = new CompositeDisposable();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivZhou = (ImageView) findViewById(R.id.iv_zhou);
        ivTu = (ImageView) findViewById(R.id.iv_tu);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvStaticNo = (TextView) findViewById(R.id.tv_static_no);
        tvDymNo = (TextView) findViewById(R.id.tv_dym_no);
        rvStatic = (RecyclerView) findViewById(R.id.rv_static);
        rvDynamic = (RecyclerView) findViewById(R.id.rv_dynamic);
        ivTu.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivZhou.setOnClickListener(this);
    }

    private void getDynamicData() {
        final Map<String, String> map = new HashMap<>();
        map.put("similarity", similarity);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("deviceCodes", deviceCodes);
        map.put("imageBase64", imageBase64);
        map.put("operator", operator);
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_DynamicBean>>() {
                    @Override
                    public ObservableSource<SW_DynamicBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.getDynamicData(map);
                        } else {
                            return null;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_DynamicBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_DynamicBean sw_dynamicBean) {
                        if (sw_dynamicBean.getRetCode() == 0) {
                            data = sw_dynamicBean.getData();
                            if (data == null || data.size() == 0) {
                                tvDymNo.setVisibility(View.VISIBLE);
                            } else {
                                changeRvTu();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvDymNo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void changeRvTu() {
        BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder> dynamicAdapter = new BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder>(R.layout.sw_item_dymnic_compare, data) {

            @Override
            protected void convert(BaseViewHolder helper, SW_DynamicBean.DataBean item) {
                helper.setText(R.id.tv_time, item.getSimilarity())
                        .setText(R.id.tv_similarity, item.getFaceTime())
                        .setText(R.id.tv_site, item.getSex());
                ImageView ivSmall = helper.getView(R.id.iv_small);
                ImageView ivBig = helper.getView(R.id.iv_big);
                Glide.with(SW_FaceSearchingResultActivity.this).load(item.getSource_image1()).placeholder(R.drawable.sw_icon_img_unselected).into(ivSmall);
                Glide.with(SW_FaceSearchingResultActivity.this).load(item.getSource_image2()).placeholder(R.drawable.sw_icon_img_unselected).into(ivBig);
            }
        };
        rvDynamic.setAdapter(dynamicAdapter);
        dynamicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SW_FaceSearchingResultActivity.this, SW_DynamicDialogActivity.class);
                intent.putExtra("datas", data.get(position));
                startActivity(intent);
            }
        });
    }

    private void changeRvZhou() {
        BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder> dynamicAdapter = new BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder>(R.layout.sw_item_time_line_style, data) {

            @Override
            protected void convert(BaseViewHolder helper, SW_DynamicBean.DataBean item) {
                helper.setText(R.id.tv_time, item.getSimilarity())
                        .setText(R.id.tv_site, item.getSex());
            }
        };
        rvDynamic.setAdapter(dynamicAdapter);
        dynamicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SW_FaceSearchingResultActivity.this, SW_DynamicDialogActivity.class);
                intent.putExtra("datas", data.get(position));
                startActivity(intent);
            }
        });
    }

    private void getStaticData() {
        final Map<String, String> map = new HashMap<>();
        map.put("similarity", similarity);
        map.put("imageBase64", imageBase64);
        map.put("operator", operator);
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_StaticBean>>() {
                    @Override
                    public ObservableSource<SW_StaticBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.getStaticData(map);
                        } else {
                            return null;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_StaticBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_StaticBean sw_staticBean) {
                        if (sw_staticBean.getRetCode() == 0) {
                            final List<SW_StaticBean.DataBean> data = sw_staticBean.getData();
                            if (data == null || data.size() == 0) {
                                tvStaticNo.setVisibility(View.VISIBLE);
                            } else {
                                BaseQuickAdapter<SW_StaticBean.DataBean, BaseViewHolder> staticAdapter = new BaseQuickAdapter<SW_StaticBean.DataBean, BaseViewHolder>(R.layout.sw_item_static_compare, data) {

                                    @Override
                                    protected void convert(BaseViewHolder helper, SW_StaticBean.DataBean item) {
                                        helper.setText(R.id.tv_time, item.getName())
                                                .setText(R.id.tv_similarity, item.getSimilarity() + "%")
                                                .setText(R.id.tv_id, item.getCardNum())
                                                .setText(R.id.tv_type, item.getLibName());

                                    }
                                };
                                rvStatic.setAdapter(staticAdapter);
                                staticAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        Intent intent = new Intent(SW_FaceSearchingResultActivity.this, SW_StaticDialogActivity.class);
                                        intent.putExtra("datas", data.get(position));
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvStaticNo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFaceParams(SW_FaceParams bean) {
        deviceCodes = bean.getDeviceCodes();
        imageBase64 = bean.getImageBase64();
        similarity = bean.getSimilarity();
        startTime = bean.getStartTime();
        endTime = bean.getEndTime();
        operator = bean.getOperator();
        LogUtils.e(deviceCodes);
        //LogUtils.e(imageBase64);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        compositeDisposable.dispose();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.iv_zhou) {
            changeRvZhou();
            ivZhou.setImageResource(R.drawable.sw_icon_zhou);
            ivTu.setImageResource(R.drawable.sw_icon_img_unselected);
        } else if (i == R.id.iv_tu) {
            changeRvTu();
            ivZhou.setImageResource(R.drawable.sw_icon_zhou_unselected);
            ivTu.setImageResource(R.drawable.sw_icon_img);
        }
    }
}
