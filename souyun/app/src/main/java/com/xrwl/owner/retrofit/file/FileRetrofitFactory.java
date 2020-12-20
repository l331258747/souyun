package com.xrwl.owner.retrofit.file;

import com.xrwl.owner.http.HttpConstants;
import com.xrwl.owner.retrofit.Api;
import com.xrwl.owner.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 文件下载
 * Created by www.longdw.com on 2018/5/24 下午4:15.
 */
public class FileRetrofitFactory {

    public static Api getInstance(JsDownloadListener listener) {
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(listener);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(HttpConstants.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder().baseUrl(Constants.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(Api.class);
    }

}
