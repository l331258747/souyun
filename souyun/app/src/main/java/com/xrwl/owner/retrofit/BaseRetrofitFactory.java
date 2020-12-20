package com.xrwl.owner.retrofit;

import android.util.Log;

import com.xrwl.owner.http.HttpConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * Created by www.longdw.com on 2017/12/19 下午3:02.
 */

public class BaseRetrofitFactory {

    public static final String TAG = BaseRetrofitFactory.class.getSimpleName();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.d(TAG,"OkHttp====Message:"+message);
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);

    public static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(logging)
//            .addInterceptor(new CommonParamsInterceptor() {
//                @Override
//                public Map<String, String> getHeaderMap() {
//                    return null;
//                }
//
//                @Override
//                public Map<String, String> getQueryParamMap(Request request) {
//
//                    return null;
//                }
//
//                @Override
//                public Map<String, String> getFormBodyParamMap(Map<String, String> originParams) {
//
//                    return null;
//                }
//
//                @Override
//                public Map<String, String> getMultiBodyParamMap() {
//                    return null;
//                }
//
//
//            })
//            .addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    if ("POST".equals(request.method())) {
//                        Request.Builder requestBuilder = request.newBuilder();
//                        request = requestBuilder
//                                .addHeader("Content-Type", "application/json;charset=UTF-8")
//                                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
//                                        bodyToString(request.body())))//关键部分，设置requestBody的编码格式为json
//                                .build();
//                        return chain.proceed(request);
//                    } else {
//                        return chain.proceed(request);
//                    }
//                }
//            })
            .connectTimeout(HttpConstants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HttpConstants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
