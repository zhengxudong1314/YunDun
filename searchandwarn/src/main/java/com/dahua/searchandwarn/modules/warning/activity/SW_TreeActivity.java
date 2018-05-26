package com.dahua.searchandwarn.modules.warning.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.adapter.SW_AddressTreeAdapter;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.base.SqlietModel;
import com.dahua.searchandwarn.model.SW_AddressTreeBean;
import com.dahua.searchandwarn.model.SW_DeviceCodeBean;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SW_TreeActivity extends AppCompatActivity {
    private RecyclerView rv;
    private RecyclerView rvEt;
    private CompositeDisposable compositeDisposable;
    private SW_AddressTreeAdapter sw_addressTreeAdapter;
    private SqlietModel sqlietModel;
    private ImageView ivBack;
    private EditText etSearch;
    private List<SW_DeviceCodeBean> addresses;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sw__tree);
        compositeDisposable = new CompositeDisposable();
        sqlietModel = new SqlietModel(this);
        rv = (RecyclerView) findViewById(R.id.rv);
        rvEt = (RecyclerView) findViewById(R.id.rv_et);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        etSearch = (EditText) findViewById(R.id.et_search);
        rvEt.setLayoutManager(new LinearLayoutManager(this));
        getNetData();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, final int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    rvEt.setVisibility(View.GONE);
                } else {
                    rvEt.setVisibility(View.VISIBLE);
                }
                addresses = sqlietModel.likeQuery(editable.toString());

                rvEt.setAdapter(new BaseQuickAdapter<SW_DeviceCodeBean, BaseViewHolder>(R.layout.sw_view_textview_item, addresses) {

                    @Override
                    protected void convert(BaseViewHolder helper, SW_DeviceCodeBean item) {
                        final String devCode = item.getDevCode();
                        final String address = item.getAddress();
                        helper.setText(R.id.tv_name, item.getAddress());
                        TextView tv_name = helper.getView(R.id.tv_name);
                        tv_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EventBus.getDefault().post(new SW_DeviceCodeBean(devCode, address));
                                finish();
                            }
                        });
                    }

                });
            }
        });
    }

    private void getNetData() {
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_AddressTreeBean>>() {
                    @Override
                    public ObservableSource<SW_AddressTreeBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.getAddressTree();
                        } else {
                            return null;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_AddressTreeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(final SW_AddressTreeBean sw_addressTreeBean) {

                        if (sw_addressTreeBean.getRetCode() == 0) {

                            rv.setLayoutManager(new LinearLayoutManager(SW_TreeActivity.this));
                            List<SW_AddressTreeBean.DataBean> data = sw_addressTreeBean.getData();

                            final List<SW_AddressTreeBean.DataBean> citys = sw_addressTreeBean.getData();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    for (int i = 0; i < citys.size(); i++) {
                                        List<SW_AddressTreeBean.DataBean.ChildrenBeanXX> qus = citys.get(i).getChildren();
                                        for (int j = 0; j < qus.size(); j++) {
                                            List<SW_AddressTreeBean.DataBean.ChildrenBeanXX.ChildrenBeanX> jies = qus.get(j).getChildren();
                                            for (int k = 0; k < jies.size(); k++) {
                                                List<SW_AddressTreeBean.DataBean.ChildrenBeanXX.ChildrenBeanX.ChildrenBean> ads = jies.get(k).getChildren();
                                                if (!TextUtils.isEmpty(jies.get(k).getDevName())) {
                                                    address = citys.get(i).getOrgName() + qus.get(j).getOrgName() + jies.get(k).getDevName();
                                                    sqlietModel.insertAddress(address, jies.get(k).getDevCode());
                                                }
                                                if (ads != null) {
                                                    for (int l = 0; l < ads.size(); l++) {
                                                        address = citys.get(i).getOrgName() + qus.get(j).getOrgName() + jies.get(k).getOrgName() + ads.get(l).getDevName();
                                                        sqlietModel.insertAddress(address, ads.get(l).getDevCode());
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
                            }).start();

                            sw_addressTreeAdapter = new SW_AddressTreeAdapter(SW_TreeActivity.this, R.layout.sw_view_textview, citys);
                            rv.setAdapter(sw_addressTreeAdapter);
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
        compositeDisposable.dispose();
    }
}
