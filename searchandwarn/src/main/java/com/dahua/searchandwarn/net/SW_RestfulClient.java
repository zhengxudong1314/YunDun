package com.dahua.searchandwarn.net;

import android.util.Log;

import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.utils.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 创建： ZXD
 * 日期 2018/5/12
 * 功能：
 */

public class SW_RestfulClient {
    private static final int TIMEOUT = 60 * 5; // 连接超时时间,单位  秒
    private volatile static SW_RestfulClient restfulClient;
    private SW_RestfulApi restfulApi;

    public static SW_RestfulClient getInstance() {
        if (restfulClient == null) {
            synchronized (SW_RestfulClient.class) {
                if (restfulClient == null) {
                    restfulClient = new SW_RestfulClient();
                }
            }
        }
        return restfulClient;
    }

    public SW_RestfulApi getRestfulApi(String baseUrl) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("zxd-----", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
                .build();
        restfulApi = retrofit.create(SW_RestfulApi.class);
        return restfulApi;
    }

}
