package com.xrwl.owner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.keeplive.KeepLive;
import com.hdgq.locationlib.keeplive.config.ForegroundNotification;
import com.hdgq.locationlib.keeplive.config.ForegroundNotificationClickListener;
import com.hdgq.locationlib.keeplive.config.KeepLiveService;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.xrwl.owner.bean.DaoMaster;
import com.xrwl.owner.bean.DaoSession;


import org.greenrobot.greendao.database.Database;

import java.io.File;

/**
 * Created by www.longdw.com on 2018/3/25 下午4:23.
 */

public class MyApplication extends Application {

    public static Context mContext;

    private DaoSession mDaoSession;
    private String rootPath = "/xrwl";
    private String downloadPath = "/download";

    public static IWXAPI mWXapi;
    public String WX_APP_ID = "wx40197add197dfbf6";

    private String enterpriseSenderCode;
    private String appSecurity;
    private String context;
    private String environment;
    private String listener;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        mContext = getApplicationContext();
        //向微信注册
        registerToWX();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "xrwl-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();





        //进程保活
        //定义前台服务的默认样式。即标题、描述和图标
        ForegroundNotification foregroundNotification = new
                ForegroundNotification("兴荣物流", "描述", R.mipmap.ic_launcher,

                //定义前台服务的通知点击事件
                new ForegroundNotificationClickListener() {
                    @Override
                    public void foregroundNotificationClick(Context context, Intent intent) {

                    }
                });
        //启动保活服务
        KeepLive.startWork(this, KeepLive.RunMode.ENERGY,
                foregroundNotification,

        new KeepLiveService() {
            /**
             14. * 运行中
             15. * 由于服务可能会多次自动启动，该方法可能重复调用
             16. */
            @Override
            public void onWorking() {

            }
            /**
             21. * 服务终止
             22. * 由于服务可能会被多次终止，该方法可能重复调用，需同 onWorking 配
             套使用，
             23. * 如注册和注销 broadcast
             24. */
            @Override
            public void onStop() {

            }
        }

       );


    }

    private void registerToWX() {
        mWXapi = WXAPIFactory.createWXAPI(mContext, WX_APP_ID, false);
        mWXapi.registerApp(WX_APP_ID);
    }

    public static Context getContext() {
        return mContext;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void initExternalPath() {
        String root;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            root = Environment.getExternalStorageDirectory().getPath();
        } else {
            root = getCacheDir().getPath();
        }
        createPath(root);
    }

    public void initInnerPath() {
        String root = getCacheDir().getPath();
        createPath(root);
    }

    private void createPath(String root) {
        rootPath = root + rootPath;
        downloadPath = rootPath + downloadPath;

        File downloadFile = new File(downloadPath);
        if (!downloadFile.exists()) {
//            Toast.makeText(mContext, "UI经启动", Toast.LENGTH_SHORT).show();
            downloadFile.mkdirs();
        }
    }


    public String getDownloadPath() {
        return downloadPath;
    }

}
