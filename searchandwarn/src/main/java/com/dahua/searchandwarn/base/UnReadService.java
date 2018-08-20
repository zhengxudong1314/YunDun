package com.dahua.searchandwarn.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dahua.searchandwarn.model.SW_HistoryWarnBean;
import com.dahua.searchandwarn.model.SW_UnReadNum;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/23
 */

public class UnReadService extends Service {
    private CompositeDisposable compositeDisposable;
    private SqlietModel sqlietModel;
    private int num = 0;
    private List<SW_HistoryWarnBean.DataBean> undisposeData;

    @Override
    public void onCreate() {
        super.onCreate();
        undisposeData = new ArrayList<>();
        sqlietModel = new SqlietModel(this);
        compositeDisposable = new CompositeDisposable();
        Map<String, String> map = new HashMap<>();
        map.put("appUser", SW_Constracts.getUserName(this));
        map.put("pageNum", "1");
        map.put("pageSize", "50");
        getNetData(map);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void getNetData(final Map<String, String> map) {
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
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
                            List<SW_HistoryWarnBean.DataBean> datas = historyWarnBean.getData();
                            if (datas != null && datas.size() > 0)
                                for (int i = 0; i < datas.size(); i++) {
                                    if (datas.get(i).getStatus().equals("待处理") || datas.get(i).getStatus().equals("处理中")) {
                                        undisposeData.add(datas.get(i));
                                    }
                                }
                            List<String> list = sqlietModel.queryAll();
                            int flag = 0;
                            for (int i = 0; i < undisposeData.size(); i++) {
                                String alarmId = undisposeData.get(i).getAlarmId();
                                if (list.size() == 0) {
                                    num = undisposeData.size();
                                } else {
                                    for (int j = 0; j < list.size(); j++) {
                                        if (alarmId.equals(list.get(j))) {
                                            flag = 0;
                                            break;
                                        } else {
                                            flag = 1;
                                        }

                                    }
                                    if (flag == 1) {
                                        num++;
                                    }
                                }

                            }
                            EventBus.getDefault().postSticky(new SW_UnReadNum(num));

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().postSticky(new SW_UnReadNum(0));

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
