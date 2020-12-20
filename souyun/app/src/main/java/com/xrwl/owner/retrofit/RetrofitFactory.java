package com.xrwl.owner.retrofit;

import com.xrwl.owner.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:29.
 */
public class RetrofitFactory extends BaseRetrofitFactory {

    private static Api mApi;

    public static Api getInstance(String host) {
        if (mApi == null) {
            mApi = new Retrofit.Builder().baseUrl(host)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build()
                    .create(Api.class);
        }
        return mApi;
    }

    public static Api getInstance() {
        if (mApi == null) {
            mApi = new Retrofit.Builder().baseUrl(Constants.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build()
                    .create(Api.class);
        }
        return mApi;
    }

}
