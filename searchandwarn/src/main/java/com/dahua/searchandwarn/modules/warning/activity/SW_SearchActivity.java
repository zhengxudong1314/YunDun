package com.dahua.searchandwarn.modules.warning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.model.SW_AddressTreeBean;
import com.dahua.searchandwarn.model.SW_DeviceCodeBean;
import com.dahua.searchandwarn.utils.TimeUtils;
import com.lvfq.pickerview.DSSTimePickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SW_SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvSite;
    private TextView tvState;
    private TextView tvSure;
    private TextView tvReset;
    private TextView tvTitle;
    private EditText etReason;
    private EditText etCardId;
    private Intent intent;

    private List<SW_AddressTreeBean.DataBean.ChildrenBeanXX.ChildrenBeanX.ChildrenBean> datas;
    private String site;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_search);
        EventBus.getDefault().register(this);
        initView();
        tvTitle.setText(getString(R.string.warn_search_condition));
        etReason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etReason.setText("");
                }
            }
        });
        etCardId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etCardId.setText("");
                }
            }
        });
    }


    private void initView() {

        intent = new Intent(this, SW_HistroyWarnActivity.class);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        tvSite = (TextView) findViewById(R.id.tv_site);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvReset = (TextView) findViewById(R.id.tv_reset);
        etReason = (EditText) findViewById(R.id.et_reason);
        etCardId = (EditText) findViewById(R.id.et_card_id);
        ivBack.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvSite.setOnClickListener(this);
        tvState.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvReset.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.tv_start_time) {
            getTimePicker(tvStartTime);
        } else if (i == R.id.tv_end_time) {
            getTimePicker(tvEndTime);
        } else if (i == R.id.tv_site) {
            startActivity(new Intent(this, SW_TreeActivity.class));
        } else if (i == R.id.tv_state) {
            showStatePopupWindow();
            setBackgroundAlpha(0.7f);
        } else if (i == R.id.tv_sure) {
            startSearch();
        } else if (i == R.id.tv_reset) {
            tvStartTime.setText(getString(R.string.choose));
            tvEndTime.setText(getString(R.string.choose));
            tvSite.setText(getString(R.string.choose));
            tvState.setText(getString(R.string.choose));
            etReason.setText(null);
            etCardId.setText(null);
            site = null;
            state = null;
        }
    }

    private void startSearch() {
        String cardId = etCardId.getText().toString().trim();
        String reason = etReason.getText().toString().trim();
        String startTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        intent.putExtra("cardId", cardId);
        intent.putExtra("reason", reason);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("site", site);
        intent.putExtra("state", state);
        startActivity(intent);
    }


    private void getTimePicker(final TextView tvTime) {
        DSSTimePickerView pickerView = new DSSTimePickerView(this, DSSTimePickerView.Type.ALL);
        pickerView.show();
        pickerView.setOnTimeSelectListener(new DSSTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvTime.setText(TimeUtils.date2String(date, new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")));
            }
        });
    }


    private StringBuilder strState;
    private StringBuilder strNum;


    private void showStatePopupWindow() {
        strState = new StringBuilder();
        strNum = new StringBuilder();
        View view = LayoutInflater.from(this).inflate(R.layout.sw_view_popup_state, null);
        TextView tvCancle = (TextView) view.findViewById(R.id.tv_cacle);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        final CheckBox cb_undispose = (CheckBox) view.findViewById(R.id.cb_undispose);
        final CheckBox cb_disposing = (CheckBox) view.findViewById(R.id.cb_disposing);
        final CheckBox cb_ignore = (CheckBox) view.findViewById(R.id.cb_ignore);
        final CheckBox cb_cacle_warn = (CheckBox) view.findViewById(R.id.cb_cacle_warn);
        final String s1 = cb_undispose.getText().toString();
        final String s2 = cb_disposing.getText().toString();
        final String s3 = cb_cacle_warn.getText().toString();
        final String s4 = cb_ignore.getText().toString();
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_undispose.isChecked()) {
                    appendStr(s1);
                    appendStrNum("1");
                }
                if (cb_disposing.isChecked()) {
                    appendStr(s2);
                    appendStrNum("2");
                }
                if (cb_cacle_warn.isChecked()) {
                    appendStrNum("3");
                    appendStr(s3);
                }
                if (cb_ignore.isChecked()) {
                    appendStrNum("4");
                    appendStr(s4);
                }
                String substring = strState.substring(1);
                String subNum = strNum.substring(1);
                state = subNum;

                tvState.setText(substring);
                popupWindow.dismiss();

            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private void appendStr(String s) {
        strState.append("/");
        strState.append(s);
    }

    private void appendStrNum(String s) {
        strNum.append(",");
        strNum.append(s);
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMood(SW_DeviceCodeBean bean) {
        tvSite.setText(bean.getAddress());
        site = bean.getDevCode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
