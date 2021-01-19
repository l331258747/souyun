package com.xrwl.owner.module.loading.mvp;

import android.content.Context;
import android.util.Log;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Update;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;
import com.xrwl.owner.retrofit.RxSchedulers;
import com.xrwl.owner.retrofit.file.JsDownloadListener;
import com.xrwl.owner.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by www.longdw.com on 2018/4/2 下午10:05.
 */

public class LoadingPresenter extends LoadingContract.APresenter {

    private LoadingContract.IModel mModel;

    public LoadingPresenter(Context context) {
        super(context);

        mModel = new LoadingModel();
    }

    @Override
    public void copyDB() {

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                copy();

                e.onNext(new Object());
            }
        }).compose(RxSchedulers.compose()).subscribe(new BaseSimpleObserver<Object>() {
            @Override
            protected void onHandleSuccess(Object entity) {
                mView.onRefreshSuccess(null);
            }

            @Override
            protected void onHandleError(Throwable e) {
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        });
    }

    @Override
    public void checkUpdate(Map<String, String> params) {
        mModel.checkUpdate(params).subscribe(new BaseObserver<Update>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Update> entity) {
                if (entity.isSuccess()) {
                    Update update = entity.getData();
                    if (update.canUpdate()) {
                        mView.onUpdateSuccess(entity);
                    } else {
                        mView.onUpdateError();
                    }
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onUpdateError();
            }
        });
    }

    @Override
    public void downloadApk(final String url, JsDownloadListener l) {

        mModel.downloadApk(url, l).subscribe(new BaseSimpleObserver<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(ResponseBody entity) {

                String path = "/storage/emulated/0/xrwl/download";
                String name = FileUtil.getFileNameWithUrl(url);

                String apkPath = path + File.separator + name;

                boolean result = writeResponseBodyToDisk(entity, apkPath);

                if (result) {
                    mView.onApkDownloadSuccess(apkPath);
                } else {
                    mView.onApkDownloadSuccess(apkPath);
//                    mView.onApkDownloadError();
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onApkDownloadError();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String apkPath) {

        try {
            File futureStudioIconFile = new File(apkPath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void copy() {
    }
}
