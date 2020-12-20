package com.xrwl.owner.module.loading.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Update;
import com.xrwl.owner.retrofit.file.JsDownloadListener;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by www.longdw.com on 2018/4/2 下午10:02.
 */

public interface LoadingContract {
    interface IView extends BaseMVP.IBaseView {
        void onUpdateSuccess(BaseEntity<Update> entity);
        void onUpdateError();

        void onApkDownloadSuccess(String apkPath);
        void onApkDownloadError();
    }

    abstract class APresenter extends BaseMVP.BasePresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void copyDB();

        public abstract void checkUpdate(Map<String, String> params);

        public abstract void downloadApk(String url, JsDownloadListener l);
    }

    interface IModel {
        Observable<BaseEntity<Update>> checkUpdate(Map<String, String> params);

        Observable<ResponseBody> downloadApk(String url, JsDownloadListener l);
    }
}
