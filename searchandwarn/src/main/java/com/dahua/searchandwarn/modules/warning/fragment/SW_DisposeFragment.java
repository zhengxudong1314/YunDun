package com.dahua.searchandwarn.modules.warning.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.adapter.SW_DisposeAdapter;
import com.dahua.searchandwarn.base.LoadingDialogUtils;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_HistoryWarnBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 * 创建： ZXD
 * 日期 2018/5/9
 * 功能：已处理预警信息
 */

public class SW_DisposeFragment extends Fragment {

    private View view;
    private RecyclerView rv;
    private SW_DisposeAdapter disposeAdapter;
    private TextView tvNoData;
    private List<SW_HistoryWarnBean.DataBean> disposeData;
    private CompositeDisposable compositeDisposable;
    private int pageNum = 1;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar pbLoading;
    private LinearLayout loadingMore;
    private TextView tvLoading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sw_fragment_dispose, null);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        loadingMore = (LinearLayout) view.findViewById(R.id.ll_loading_more);
        tvLoading = (TextView) view.findViewById(R.id.tv_loading);
        pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        tvNoData = (TextView) view.findViewById(R.id.tv_no_data);
        disposeData = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
        getNetData();
        tvLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNum++;
                pbLoading.setVisibility(View.VISIBLE);
                tvLoading.setText("加载中");
                tvLoading.setFocusable(false);
                getNetData();
            }
        });
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取最后一个可见的自子条目位置
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                //获取所有子条目总数
                int itemCount = linearLayoutManager.getItemCount();
                //判断是否滑动到底部
                if (lastVisibleItemPosition == (itemCount - 1)) {
                    tvLoading.setFocusable(true);
                    pbLoading.setVisibility(View.GONE);
                    tvLoading.setText("点击加载更多");
                    loadingMore.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void getNetData() {
        final Map<String, String> map = new HashMap<>();
        map.put("appUser", SW_Constracts.getUserName(getActivity()));
        map.put("pageNum", pageNum+"");
        map.put("pageSize", "50");
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(getActivity()));
        restfulApi.getHistoryWarn(map).subscribeOn(Schedulers.io())
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

                            loadingMore.setVisibility(View.GONE);
                            LoadingDialogUtils.dismiss();
                            List<SW_HistoryWarnBean.DataBean> datas = historyWarnBean.getData();
                            if (datas != null && datas.size() > 0)
                            for (int i = 0; i < datas.size(); i++) {
                                if (datas.get(i).getStatus().equals("已忽略") || datas.get(i).getStatus().equals("已消警")) {
                                    disposeData.add(datas.get(i));
                                }
                            }
                            Collections.sort(disposeData, new SW_DisposeFragment.DateComparator());
                            disposeAdapter = new SW_DisposeAdapter(getActivity(), R.layout.sw_item_dispose, disposeData);
                            rv.setLayoutManager(linearLayoutManager);
                            if (disposeData == null || disposeData.size() == 0) {
                                tvNoData.setVisibility(View.VISIBLE);
                            } else {
                                tvNoData.setVisibility(View.GONE);
                                rv.setAdapter(disposeAdapter);
                            }

                        } else {
                            LoadingDialogUtils.dismiss();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        tvNoData.setVisibility(View.VISIBLE);

                        loadingMore.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMoon(SW_HistoryWarnBean.DataBean dataBean){
        disposeData.add(0,dataBean);
        if (disposeAdapter!=null){
            disposeAdapter.notifyDataSetChanged();
        }
    }

    private static class DateComparator implements Comparator<SW_HistoryWarnBean.DataBean> {

        @Override
        public int compare(SW_HistoryWarnBean.DataBean dataBean, SW_HistoryWarnBean.DataBean t1) {
            Date date1 = stringToDate(dataBean.getSaveTime());
            Date date2 = stringToDate(t1.getSaveTime());
            // 对日期字段进行升序，如果欲降序可采用after方法
            if (dataBean.getSaveTime().equals(t1.getSaveTime())){
                return 0;
            }
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
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
