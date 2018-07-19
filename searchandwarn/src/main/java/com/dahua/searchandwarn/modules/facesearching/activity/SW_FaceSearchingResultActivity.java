package com.dahua.searchandwarn.modules.facesearching.activity;

import android.app.ProgressDialog;
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
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.TwoPointUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
    private ImageView ivTu;
    private ImageView ivZhou;
    private RecyclerView rvStatic;
    private RecyclerView rvDynamic;
    private List<SW_DynamicBean.DataBean> dataTu;
    private List<SW_DynamicBean.DataBean> dataZhou;
    private ProgressDialog progressDialog;
    private TextView tvDynamic;
    private TextView tvStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_face_searching_result);
        EventBus.getDefault().register(this);
        initView();
        tvTitle.setText("人脸比对");
        rvDynamic.setLayoutManager(new LinearLayoutManager(this));
        rvStatic.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getStaticData();
        getDynamicData();
    }

    private void initView() {
        compositeDisposable = new CompositeDisposable();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivZhou = (ImageView) findViewById(R.id.iv_zhou);
        ivTu = (ImageView) findViewById(R.id.iv_tu);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvStatic = (TextView) findViewById(R.id.tv_static);
        tvDynamic = (TextView) findViewById(R.id.tv_dynamic);
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
        restfulApi.getDynamicData(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_DynamicBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_DynamicBean sw_dynamicBean) {
                        if (sw_dynamicBean.getRetCode() == 0) {
                            dataTu = sw_dynamicBean.getData();
                            dataZhou = sw_dynamicBean.getData();
                            if (dataTu == null || dataTu.size() == 0) {
                                tvDynamic.setVisibility(View.VISIBLE);
                                rvDynamic.setVisibility(View.GONE);
                            } else {
                                tvDynamic.setVisibility(View.GONE);
                                rvDynamic.setVisibility(View.VISIBLE);
                                changeRvTu();
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    private void changeRvTu() {
        if (dataTu != null) {
            BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder> dynamicAdapter = new BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder>(R.layout.sw_item_dymnic_compare, dataTu) {

                @Override
                protected void convert(BaseViewHolder helper, SW_DynamicBean.DataBean item) {
                    helper.setText(R.id.tv_time, item.getFaceTime())
                            .setText(R.id.tv_similarity, TwoPointUtils.doubleToString(Double.valueOf(item.getSimilarity())) + "%")
                            .setText(R.id.tv_site, item.getDevName());
                    ImageView ivImg = helper.getView(R.id.iv_img);
                    Glide.with(SW_FaceSearchingResultActivity.this).load(item.getSource_image1()).placeholder(R.drawable.sw_home_page_defect).into(ivImg);
                }
            };
            rvDynamic.setAdapter(dynamicAdapter);
            dynamicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(SW_FaceSearchingResultActivity.this, SW_DynamicDialogActivity.class);
                    intent.putExtra("datas", dataTu.get(position));
                    startActivity(intent);
                }
            });
        }
    }

    private void changeRvZhou() {
        if (dataZhou != null) {
            Collections.sort(dataZhou, new DateComparator());
            BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder> dynamicAdapter = new BaseQuickAdapter<SW_DynamicBean.DataBean, BaseViewHolder>(R.layout.sw_item_time_line_style, dataZhou) {

                @Override
                protected void convert(BaseViewHolder helper, SW_DynamicBean.DataBean item) {
                    helper.setText(R.id.tv_time, item.getFaceTime())
                            .setText(R.id.tv_site, item.getDevName());
                    ImageView iv_small = helper.getView(R.id.iv_small);
                    Glide.with(SW_FaceSearchingResultActivity.this).load(item.getSource_image1()).placeholder(R.drawable.sw_home_page_defect).into(iv_small);
                }
            };
            rvDynamic.setAdapter(dynamicAdapter);
            dynamicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(SW_FaceSearchingResultActivity.this, SW_DynamicDialogActivity.class);
                    intent.putExtra("datas", dataZhou.get(position));
                    startActivity(intent);
                }
            });
        }
    }

    private void getStaticData() {
        final Map<String, String> map = new HashMap<>();
        map.put("similarity", similarity);
        map.put("imgBase64", imageBase64);
        map.put("operator", operator);
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.getStaticData(map).subscribeOn(Schedulers.io())
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
                                tvStatic.setVisibility(View.VISIBLE);
                                rvStatic.setVisibility(View.GONE);
                            } else {
                                tvStatic.setVisibility(View.GONE);
                                rvStatic.setVisibility(View.VISIBLE);
                                BaseQuickAdapter<SW_StaticBean.DataBean, BaseViewHolder> staticAdapter = new BaseQuickAdapter<SW_StaticBean.DataBean, BaseViewHolder>(R.layout.sw_item_static_compare, data) {

                                    @Override
                                    protected void convert(BaseViewHolder helper, SW_StaticBean.DataBean item) {
                                        helper.setText(R.id.tv_time, item.getName())
                                                .setText(R.id.tv_similarity, TwoPointUtils.doubleToString(item.getSimilarity()) + "%")
                                                .setText(R.id.tv_id, item.getCardNum())
                                                .setText(R.id.tv_type, item.getLibName());
                                        ImageView iv_img = helper.getView(R.id.iv_img);
                                        Glide.with(SW_FaceSearchingResultActivity.this).load(item.getFace()).placeholder(R.drawable.sw_home_page_defect).into(iv_img);

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
                        } else {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog!=null){
            progressDialog.dismiss();
            progressDialog=null;
        }
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

    private static class DateComparator implements Comparator<SW_DynamicBean.DataBean> {
        @Override
        public int compare(SW_DynamicBean.DataBean dataBean, SW_DynamicBean.DataBean t1) {
            Date date1 = stringToDate(dataBean.getFaceTime());
            Date date2 = stringToDate(t1.getFaceTime());
            // 对日期字段进行升序，如果欲降序可采用after方法
            if (date1.before(date2)) {
                return 1;
            }
            return -1;
        }
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }
}
