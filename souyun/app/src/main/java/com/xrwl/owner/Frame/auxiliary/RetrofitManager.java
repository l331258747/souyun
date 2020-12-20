package com.xrwl.owner.Frame.auxiliary;


import com.ldw.library.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 单例Retrofit网络请求封装
 * author:neijin
 * 2018/8/30
 **/
public class RetrofitManager {

    private static RetrofitManager mRetrofitManager;
    private Retrofit mRetrofit;
    private static final byte[] LOCKER = new byte[0];

    public RetrofitManager() {
        initRetrofit();
    }

    public static RetrofitManager getInstance() {
        if (mRetrofitManager == null) {
            synchronized (LOCKER) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager();
                }
            }
        }
        return mRetrofitManager;
    }


    private void initRetrofit() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);


        //创建网络拦截器
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LoggerInterceptor());//使用自定义的Log拦截器

        }


        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

    }

    public <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }

}





