package com.dahua.searchandwarn.base;

import android.app.Activity;
import android.app.ProgressDialog;

import java.lang.ref.WeakReference;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/20
 */

public class LoadingDialogUtils {
    /**
     * 数据访问等待框
     */
    private static ProgressDialog loadingDialog;
    private static WeakReference<Activity> reference;

    public static void init(Activity act) {
        if (loadingDialog == null || reference == null || reference.get() == null || reference.get().isFinishing()) {
            reference = new WeakReference<>(act);
            loadingDialog = new ProgressDialog(reference.get());
            loadingDialog.setMessage("加载中...");
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void setCancelable(boolean b) {
        if (loadingDialog == null) return;
        loadingDialog.setCancelable(b);
    }

    /**
     * 显示等待框
     */
    public static void show(Activity act) {
        init(act);
        loadingDialog.show();
    }

    /**
     * 隐藏等待框
     */
    public static void dismiss() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 注销加载框，避免发生内存泄露
     */
    public static void unInit() {
        dismiss();
        loadingDialog = null;
        reference = null;
    }
}


