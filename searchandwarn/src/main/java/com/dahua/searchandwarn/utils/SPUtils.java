package com.dahua.searchandwarn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by you on 2016/10/9.
 * <p>
 * SPUtils
 */
public class SPUtils {


    /**
     * 保存在手机里面的文件名
     */
    private static final String SP_FILE_NAME = "com_hd_patrol_sp_file_name";


    /**
     * 用户Session值
     */

    private SPUtils() {
    }

   /* public static void put(String key, Object value) {
        put(MyApplication.getInstance().getApplicationContext(), key, value);
    }

    public static Object get(String key, Object defValue) {
        return get(MyApplication.getInstance().getApplicationContext(), key, defValue);
    }*/

    /**
     * 保存数据
     */
    public static boolean put(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value == null) {
            value = "";
        }
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        return editor.commit();
    }

    /**
     * 获取数据
     */
    public static Object get(Context context, String key, @NonNull Object defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     * 移除数据
     */
    public static boolean remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 清空保存数据
     */
    public static boolean clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }

    //将List转换成String
    public static String List2String(List SceneList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    //将String转换成List
    public static List String2List(String ListString) throws IOException, ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(ListString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List mList = (List) objectInputStream.readObject();
        objectInputStream.close();
        return mList;
    }

    /**
     * 保存对象
     */
    public static boolean putObject(Context context, Object object, String key) {
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(context);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = share.edit();
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }

    /**
     * 获取对象
     */
    public static Object getObject(Context context, String key) {
        SharedPreferences sharePre = PreferenceManager
                .getDefaultSharedPreferences(context);
        try {
            String wordBase64 = sharePre.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
