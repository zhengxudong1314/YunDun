package com.dahua.searchandwarn.weight;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.dahua.searchandwarn.R;




/**
 * app dialog
 *
 * @author sswukang on 2017/4/6 15:03
 */
public class SW_AppDialog extends Dialog {
    private SW_AppDialog(Context context, @LayoutRes int layoutRes, int width, int height) {
        super(context);
        //super(context, R.style.SW_AppDialog);
        // set content
        setContentView(layoutRes);

        Window window = getWindow();
        if (window != null) {

            WindowManager.LayoutParams params = window.getAttributes();
            if (width > 0) {
                params.width = width;
            } else {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            if (height > 0) {
                params.height = height;
            } else {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            params.gravity = Gravity.CENTER;

            window.setAttributes(params);
        }
    }
    private SW_AppDialog(Context context, @LayoutRes int layoutRes) {
        super(context);
        //super(context, R.style.AppDialog_noShape);
        // set content
        setContentView(layoutRes);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }


    /**
     * 双按钮（确认类型）dialog
     */
    public static SW_AppDialog confirm(Context context, String title, String msg, String left, String right,
                                       final View.OnClickListener leftListener, final View.OnClickListener rightListener) {
        final SW_AppDialog dialog = new SW_AppDialog(context, R.layout.sw_view_dialog_confirm, 0, 0);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        if (tvTitle != null) {
            if (!TextUtils.isEmpty(title))
                tvTitle.setText(title);
        }

        EditText tvMsg = (EditText) dialog.findViewById(R.id.tv_message);
        if (tvMsg != null) {
            if (!TextUtils.isEmpty(msg))
                tvMsg.setText(msg);
        }

        Button cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        if (cancel != null) {
            if (!TextUtils.isEmpty(left))
                cancel.setText(left);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (leftListener != null) {
                        leftListener.onClick(view);
                    }
                    dialog.dismiss();
                }
            });
            /*cancel.setOnClickListener(v -> {
                if (leftListener != null) {
                    leftListener.onClick(v);
                }
                dialog.dismiss();
            });*/
        }

        Button confirm = (Button) dialog.findViewById(R.id.bt_confirm);
        if (confirm != null) {
            if (!TextUtils.isEmpty(right))
                confirm.setText(right);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rightListener != null)
                        rightListener.onClick(view);
                    dialog.dismiss();
                }
            });
            /*confirm.setOnClickListener(v -> {
                if (rightListener != null)
                    rightListener.onClick(v);
                dialog.dismiss();
            });*/
        }

        return dialog;
    }

    /**
     * 自定义dialog1
     */
    public static SW_AppDialog customAppDialog1(Context context, @LayoutRes int layoutRes) {
        return new SW_AppDialog(context, layoutRes);
    }
    /**
     * 自定义dialog
     */
    public static SW_AppDialog customAppDialog(Context context, @LayoutRes int layoutRes) {

        return new SW_AppDialog(context, layoutRes, 0, 0);
    }
}
