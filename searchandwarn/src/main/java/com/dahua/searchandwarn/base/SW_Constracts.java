package com.dahua.searchandwarn.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/18
 */

public class SW_Constracts {
    // 10.23.10.35  10.209.252.179
    public static String baseUrl = "http://10.23.10.35:80";

    public static String getBaseUrl(Context context) {
        SharedPreferences sp = context.getSharedPreferences("dh_data", Context.MODE_PRIVATE);
        /*String ip = sp.getString("SERVER_IP_HELP","61.128.209.66");
        String port = sp.getString("SERVER_PORT_HELP","81");*/
        String ip = sp.getString("SERVER_PORT_HELP", "61.128.209.66");
        String port = sp.getString("SERVER_IP_HELP", "81");
        // fixme
        baseUrl = "http://" + ip + ":" + port;
        return baseUrl;
    }

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("dh_data", Context.MODE_PRIVATE);
        String userName = sp.getString("USER_NAME_HELP", "gb02");
        return userName;
    }
}
