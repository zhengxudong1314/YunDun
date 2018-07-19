package com.dahua.searchandwarn.modules.warning.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SW_TreeActivity extends AppCompatActivity {
    private RecyclerView rv;
    private RecyclerView rvEt;
    private CompositeDisposable compositeDisposable;
    private SqlietModel sqlietModel;
    private ImageView ivBack;
    private EditText etSearch;
    private List<SW_DeviceCodeBean> addresses;
    private boolean onBind;
    private TextView tvSure;
    private StringBuffer endAddress;
    private StringBuffer endCode;
    private List<SW_AddressTreeBean.BaseInfo> baseInfos;
    private List<SW_AddressTreeBean.BaseInfo> allBaseInfos;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sw__tree);
        compositeDisposable = new CompositeDisposable();
        sqlietModel = new SqlietModel(this);
        rv = (RecyclerView) findViewById(R.id.rv);
        rvEt = (RecyclerView) findViewById(R.id.rv_et);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        etSearch = (EditText) findViewById(R.id.et_search);
        allBaseInfos = new ArrayList<>();
        endAddress = new StringBuffer();
        endCode = new StringBuffer();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rvEt.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getNetData();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addresses != null) {
                    for (int i = 0; i < addresses.size(); i++) {
                        if (addresses.get(i).isChecked()) {
                            endAddress.append(",");
                            endAddress.append(addresses.get(i).getAddress());
                            endCode.append(",");
                            endCode.append(addresses.get(i).getDevCode());
                        }
                    }
                }

                if (allBaseInfos != null) {
                    for (SW_AddressTreeBean.BaseInfo bos : allBaseInfos) {
                        if (bos.isChecked() && !TextUtils.isEmpty(bos.getDevName())) {
                            endAddress.append(",");
                            endAddress.append(bos.getDevName());
                            endCode.append(",");
                            endCode.append(bos.getDevCode());
                        }
                    }
                }
                String subAddress = null;
                String subCode = null;
                if (endAddress.length() > 0 && endAddress != null) {
                    subAddress = endAddress.substring(1);
                }

                if (endCode.length() > 0 && endCode != null) {
                    subCode = endCode.substring(1);
                }
                SW_DeviceCodeBean deviceCodeBean = new SW_DeviceCodeBean();
                deviceCodeBean.setDevCode(subCode);
                deviceCodeBean.setAddress(subAddress);
                EventBus.getDefault().postSticky(deviceCodeBean);
                finish();
            }
        });
        searchChange();
    }

    private void searchChange() {
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
                    protected void convert(BaseViewHolder helper, final SW_DeviceCodeBean item) {
                        onBind = true;
                        helper.setText(R.id.cb_name, item.getAddress()).setChecked(R.id.cb_name, item.isChecked());
                        CheckBox cb_name = helper.getView(R.id.cb_name);
                        onBind = false;
                        cb_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (!onBind) {
                                    if (b) {
                                        item.setChecked(true);
                                    } else {
                                        item.setChecked(false);
                                    }
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }

                });
            }
        });
    }

    private void filter(SW_AddressTreeBean.BaseInfo bean) {
        if (bean.getChildren() != null) {
            for (SW_AddressTreeBean.BaseInfo baseInfo : bean.getChildren()) {
                allBaseInfos.add(baseInfo);
                baseInfo.setOrg_id(bean.getOrgCode());
                if (!TextUtils.isEmpty(baseInfo.getDevName()) && !TextUtils.isEmpty(baseInfo.getDevCode())) {
                    sqlietModel.insertAddress(baseInfo.getDevName(), baseInfo.getDevCode());
                }
                filter(baseInfo);
            }
        }
    }

    private void getNetData() {
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.getAddressTree().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_AddressTreeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_AddressTreeBean sw_addressTreeBean) {
                        if (sw_addressTreeBean.getRetCode() == 0) {
                            baseInfos = sw_addressTreeBean.getData();
                            if (baseInfos != null) {
                                SW_AddressTreeAdapter treeAdapter = new SW_AddressTreeAdapter(SW_TreeActivity.this, R.layout.sw_item_address_tree, baseInfos);
                                rv.setAdapter(treeAdapter);
                                for (SW_AddressTreeBean.BaseInfo info : baseInfos) {
                                    filter(info);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        ToastUtils.showShort("组织树获取失败");
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
