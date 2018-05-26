package com.dahua.searchandwarn.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dahua.searchandwarn.model.SW_DeviceCodeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/21
 */

public class SqlietModel {

    private SW_SqliteHelper sw_sqliteHelper;
    private SQLiteDatabase db;

    public SqlietModel(Context context) {
        sw_sqliteHelper = new SW_SqliteHelper(context);
        db = sw_sqliteHelper.getReadableDatabase();
    }

    public void insertData(String alarmId) {
        int flag = 0;
        ContentValues contentValues = new ContentValues();
        List<String> list = queryAll();
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(alarmId)) {
                    flag = 1;
                    break;
                } else {
                    flag = 0;
                }
            }
        }
        if (flag == 0) {
            contentValues.put("alarmId", alarmId);
            db.insert("table_sw", null, contentValues);

        }
    }

    public List<String> queryAll() {
        List<String> list = new ArrayList<>();
        Cursor cursor = db.query("table_sw", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String alarmId = cursor.getString(cursor.getColumnIndex("alarmId"));
            list.add(alarmId);
        }
        return list;
    }
    public  void insertAddress(String address,String devcode) {
        int flag = 0;
        ContentValues contentValues = new ContentValues();
        List<SW_DeviceCodeBean> list = queryAddress();

        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDevCode().equals(devcode)) {
                    flag = 1;
                    break;
                } else {
                    flag = 0;
                }
            }
        }
        if (flag == 0) {
            contentValues.put("address", address);
            contentValues.put("devcode", devcode);
            db.insert("table_address", null, contentValues);
        }
    }

    public List<SW_DeviceCodeBean> queryAddress() {
        List<SW_DeviceCodeBean> list = new ArrayList<>();
        Cursor cursor = db.query("table_address", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String devcode = cursor.getString(cursor.getColumnIndex("devcode"));
            SW_DeviceCodeBean sw_deviceCodeBean = new SW_DeviceCodeBean();
            sw_deviceCodeBean.setAddress(address);
            sw_deviceCodeBean.setDevCode(devcode);
            list.add(sw_deviceCodeBean);
        }

        return list;
    }

    public List<SW_DeviceCodeBean> likeQuery(String keyword) {
        List<SW_DeviceCodeBean> list = new ArrayList<>();

        String[] selectioinArgs = {"%"+keyword+"%"};//注意：这里没有单引号
        String sql = "select * from table_address where address like ?";
        Cursor cursor = db.rawQuery(sql, selectioinArgs);
        while (cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String devcode = cursor.getString(cursor.getColumnIndex("devcode"));
            SW_DeviceCodeBean sw_deviceCodeBean = new SW_DeviceCodeBean();
            sw_deviceCodeBean.setAddress(address);
            sw_deviceCodeBean.setDevCode(devcode);
            list.add(sw_deviceCodeBean);
        }

        return list;
    }
}
