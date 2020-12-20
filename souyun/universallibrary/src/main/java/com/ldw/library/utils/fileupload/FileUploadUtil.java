package com.ldw.library.utils.fileupload;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by www.longdw.com on 16/10/10 下午3:29.
 */

public class FileUploadUtil {

    private static final String TAG = FileUploadUtil.class.getSimpleName();

    private static final long DEFAULT_MILLISECONDS = 10_000L;

    private volatile static FileUploadUtil mInstance;
    private static Call mCall;

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }

    private static FileUploadUtil getInstance() {
        if (mInstance == null) {
            synchronized (FileUploadUtil.class) {
                if (mInstance == null) {
                    mInstance = new FileUploadUtil();
                }
            }
        }
        return mInstance;
    }

    public FileUploadUtil() {

    }

    public static class Builder {

        private Platform mPlatform;
        private List<FileInput> mFiles;
        private HashMap<String, String> mParams;
        private FileUploadCallback mCallback;
        private OkHttpClient mOkHttpClient;
        private Request mRequest;

        private Request.Builder mRequestBuilder = new Request.Builder();

        public Builder() {
            mPlatform = Platform.get();
            mOkHttpClient = new OkHttpClient();
            mOkHttpClient
                    .newBuilder()
                    .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        }

        public Builder host(String host) {
            mRequestBuilder.url(host);
            return this;
        }

        public Builder params(HashMap<String, String> params) {
            mParams = params;
            return this;
        }

        public Builder files(List<FileInput> files) {
            mFiles = files;
            return this;
        }

        public Builder callback(FileUploadCallback callback) {
            mCallback = callback;
            return this;
        }

        private String guessMimeType(String path) {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String contentTypeFor = null;
            try {
                contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            if (contentTypeFor == null) {
                contentTypeFor = "application/octet-stream";
            }
            return contentTypeFor;
        }

        public Builder build() {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            //添加参数
            if (mParams != null && !mParams.isEmpty()) {
                for (String key : mParams.keySet()) {

                    String value = mParams.get(key);
                    String result = "";
                    try {
                        result = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }

                    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                            RequestBody.create(null, result));
                }
            }

            if (mFiles != null) {
                for (int i = 0; i < mFiles.size(); i++) {
                    FileInput fileInput = mFiles.get(i);
                    RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                    builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
                }
            }

            RequestBody requestBody = builder.build();

            if (requestBody != null) {
                requestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
                    @Override
                    public void onRequestProgress(final long bytesWritten, final long contentLength) {
                        mPlatform.defaultCallbackExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mCallback != null)
                                    mCallback.inProgress(bytesWritten * 1.0f / contentLength, contentLength);
                            }
                        });
                    }
                });
            }

            mRequest = mRequestBuilder.post(requestBody).build();
            mCall = mOkHttpClient.newCall(mRequest);
            return this;
        }

        public FileUploadUtil execute() {
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {

                    mPlatform.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (mCallback != null) {
                                mCallback.onError(e);
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String msg = response.body().string();
                    Log.d("post res", msg);
                    mPlatform.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject(msg);
                                mCallback.onSuccess(obj);
                            } catch (Exception e) {
                                mCallback.onError(e);
                            }
                        }
                    });
                }
            });
            return FileUploadUtil.getInstance();
        }
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

}
