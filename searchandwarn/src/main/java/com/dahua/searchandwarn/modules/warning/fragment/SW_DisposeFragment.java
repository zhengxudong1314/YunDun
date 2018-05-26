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
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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

    @Override
    public void onResume() {
        super.onResume();
        pageNum=1;
        getNetData();
    }

    private void getNetData() {
        final Map<String, String> map = new HashMap<>();
        // todo  替换用户名为 SW_UserLoginBean.USERNANE
        map.put("appUser", SW_UserLoginBean.USERNANE);
        map.put("pageNum", pageNum+"");
        map.put("pageSize", "50");
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(getActivity()));
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

                            loadingMore.setVisibility(View.GONE);
                            LoadingDialogUtils.dismiss();
                            List<SW_HistoryWarnBean.DataBean> datas = historyWarnBean.getData();
                            for (int i = 0; i < datas.size(); i++) {
                                if (datas.get(i).getStatus().equals("已忽略") || datas.get(i).getStatus().equals("已消警")) {
                                    disposeData.add(datas.get(i));
                                }
                            }

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
                            ToastUtils.showLong("忽略失败");
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().postSticky("failure");
                        tvNoData.setVisibility(View.VISIBLE);

                        loadingMore.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
