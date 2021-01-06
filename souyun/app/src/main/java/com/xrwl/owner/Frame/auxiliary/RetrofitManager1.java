package com.xrwl.owner.Frame.auxiliary;

import com.ldw.library.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2020/4/24.
 */

public class RetrofitManager1 {
    private static RetrofitManager1 mRetrofitManager1;
    private Retrofit mRetrofit;
    private static final byte[] LOCKER = new byte[0];

    public RetrofitManager1() {
        initRetrofit();
    }

    public static RetrofitManager1 getInstance() {
        if (mRetrofitManager1 == null) {
            synchronized (LOCKER) {
                if (mRetrofitManager1 == null) {
                    mRetrofitManager1 = new RetrofitManager1();
                }
            }
        }
        return mRetrofitManager1;
    }


    private void initRetrofit() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);


        //创建网络拦截器
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LoggerInterceptor());//使用自定义的Log拦截器

        }


        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

    }

    public <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }

}


