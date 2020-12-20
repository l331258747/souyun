package com.xrwl.owner.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ldw.library.utils.Utils;
import com.ldw.library.utils.filedownload.FileDownloadTask;
import com.ldw.library.utils.filedownload.OnFileDownloadListener;
import com.xrwl.owner.R;
import com.xrwl.owner.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by www.longdw.com on 2016/10/26 下午4:01.
 */

public class FileDownloadService extends Service implements OnFileDownloadListener {

    public static final String TAG = FileDownloadTask.class.getSimpleName();

    private String RETRY_BROADCAST_NAME;
    private String OPEN_BROADCAST_NAME;

    private int mNotifyId = 1;
    private List<FileDownloadTask> mTaskList = new ArrayList<>();
    private List<OnFileDownloadListener> mListenerList = new ArrayList<>();
    private HashMap<String, Notification> mNotiMap = new HashMap<>();
    private HashMap<String, Integer> mNotiIdMap = new HashMap<>();
    private HashMap<String, String> mCompletedFilePath = new HashMap<>();

    private NotificationManager mNotificationManager;
    private RetryBroadcast mRetryBroadcast;

    private static String mDownloadPath = "";

    public static ServiceConnection startDownload(final Context context, String attachUrl, final ServiceConnection conn, boolean isBinded) throws Exception {

        if (TextUtils.isEmpty(mDownloadPath)) {
            throw new Exception("must call init method first.");
        }

        ServiceConnection serviceConnection = null;
        final Intent intent = new Intent(context, FileDownloadService.class);
        intent.putExtra("attach_url", attachUrl);
        if (isBinded) {
            context.startService(intent);
        } else {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    conn.onServiceConnected(name, service);
                    context.startService(intent);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    conn.onServiceDisconnected(name);
                }
            };
            //0不会新建服务 Context.BIND_AUTO_CREATE每次都会创建新的Service
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        return serviceConnection;
    }

    /**
     * 使用该类的时候需要先初始化绑定
     *
     * @return
     */
    public static ServiceConnection startBind(final Context context, final ServiceConnection conn) {
        final Intent intent = new Intent(context, FileDownloadService.class);
        intent.putExtra("method", "bind");

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                context.startService(intent);

                conn.onServiceConnected(name, service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                conn.onServiceDisconnected(name);
            }
        };
        //0不会新建服务 Context.BIND_AUTO_CREATE每次都会创建新的Service
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        return serviceConnection;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        RETRY_BROADCAST_NAME = getPackageName() + ".retry_broadcast_name";
        OPEN_BROADCAST_NAME = getPackageName() + ".open_broadcast_name";
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mRetryBroadcast = new RetryBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RETRY_BROADCAST_NAME);
        filter.addAction(OPEN_BROADCAST_NAME);
        registerReceiver(mRetryBroadcast, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }

        String method = intent.getStringExtra("method");
        if (!TextUtils.isEmpty(method) && method.equals("bind")) {
            return START_STICKY;
        }

        String attachUrl = intent.getStringExtra("attach_url");
        if (TextUtils.isEmpty(attachUrl)) {
            Toast.makeText(this, getString(R.string.pms_notify_download_url_error), Toast.LENGTH_SHORT).show();
            return START_STICKY;
        }

        if (!checkTaskExist(attachUrl)) {

            String savedPath = mDownloadPath;
            File savedFolder = new File(savedPath);
            if (!savedFolder.exists()) {
                savedFolder.mkdirs();
            }

            final File file = new File(savedPath, Utils.getFileNameFromUrl(attachUrl));
            if (file.exists()) {
                for (OnFileDownloadListener l : mListenerList) {
                    l.onDownloadCompleted(file, attachUrl);
                }

            } else {
                FileDownloadTask downloadTask = new FileDownloadTask(this, attachUrl, savedPath, this);
                downloadTask.execute();
                mTaskList.add(downloadTask);
                Toast.makeText(this, getString(R.string.pms_downloading), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, getString(R.string.pms_downloading_donotworry), Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }

    private void showNotify(String attachUrl) {

        mNotifyId++;

        if (mNotiMap.get(attachUrl) != null) {
            Notification notification = mNotiMap.get(attachUrl);
            notification.contentView.setViewVisibility(R.id.notify_download_iv, View.GONE);
            notification.contentView.setViewVisibility(R.id.notify_completed_tv, View.GONE);
            notification.contentView.setViewVisibility(R.id.notify_speed_tv, View.VISIBLE);
            notification.contentView.setTextViewText(R.id.notify_speed_tv, "");
            mNotificationManager.notify(mNotiIdMap.get(attachUrl), notification);
            return;
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_layout);
        String fileName = new File(attachUrl).getName();
        remoteViews.setTextViewText(R.id.notify_title_tv, fileName);

        Intent retryIntent = new Intent(RETRY_BROADCAST_NAME);
        retryIntent.putExtra("attach_url", attachUrl);
        PendingIntent retryPi = PendingIntent.getBroadcast(this, mNotifyId, retryIntent, PendingIntent.FLAG_UPDATE_CURRENT);//id必须不一样，否则会被之前的Intent覆盖
        remoteViews.setOnClickPendingIntent(R.id.notify_download_iv, retryPi);

        Intent intent = new Intent(OPEN_BROADCAST_NAME);
        intent.putExtra("attach_url", attachUrl);
        PendingIntent pi = PendingIntent.getBroadcast(this, mNotifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);//id必须不一样，否则会被之前的Intent覆盖
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //注意：小米手机在状态栏并没有看到效果 因为MIUI是用三个点告诉用户有新通知
        builder.setSmallIcon(R.drawable.ul_ic_download)
                .setTicker(getString(R.string.pms_notify_downloading))
                .setOngoing(true)
                .setContentIntent(pi)
                .setContent(remoteViews);
        Notification notification = builder.build();
        mNotificationManager.notify(mNotifyId, notification);//显示的时候这个id必须不一样，要不然会覆盖之前的

        mNotiMap.put(attachUrl, notification);
        mNotiIdMap.put(attachUrl, mNotifyId);
    }

    public void cancelDownload(String attachUrl) {
        for (FileDownloadTask task : mTaskList) {
            if (task.getUrl().equals(attachUrl)) {
                task.cancel();
                break;
            }
        }
    }

    private class RetryBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String attachUrl = intent.getStringExtra("attach_url");
            String action = intent.getAction();
            if (action.equals(RETRY_BROADCAST_NAME)) {
                if (!checkTaskExist(attachUrl)) {
                    String savedPath = mDownloadPath;
                    FileDownloadTask downloadTask = new FileDownloadTask(FileDownloadService.this, attachUrl, savedPath,
                            FileDownloadService.this);
                    downloadTask.execute();
                    mTaskList.add(downloadTask);
                }
            } else if (action.equals(OPEN_BROADCAST_NAME)) {
                if (attachUrl.endsWith(".apk")) {
                    FileUtil.installApk(FileDownloadService.this, mCompletedFilePath.get(attachUrl));
                } else {
                    FileUtil.checkOpenFile(FileDownloadService.this, mCompletedFilePath.get(attachUrl));
                }

                mCompletedFilePath.remove(attachUrl);
            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRetryBroadcast != null) {
            unregisterReceiver(mRetryBroadcast);
        }
    }

    public class ServiceBinder extends Binder {
        public FileDownloadService getService() {
            return FileDownloadService.this;
        }
    }

    public void setOnFileDownloadListener(OnFileDownloadListener l) {
        if (l != null && !mListenerList.contains(l)) {
            mListenerList.add(l);
        }
    }

    public void removeListener(OnFileDownloadListener l) {
        mListenerList.remove(l);
    }

    public boolean checkTaskExist(String url) {
        for (FileDownloadTask task : mTaskList) {
            if (task.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    private void deleteTask(String url) {
        for (FileDownloadTask task : mTaskList) {
            if (task.getUrl().equals(url)) {
                mTaskList.remove(task);
                break;
            }
        }
    }

    @Override
    public void onDownloadStart(String attachUrl) {
//        Log.e(TAG, "download start");
        for (OnFileDownloadListener l : mListenerList) {
            l.onDownloadStart(attachUrl);
        }

        showNotify(attachUrl);
    }

    @Override
    public void onDownloading(long total, long current, String percent, int progress, String speed, String attachUrl) {

        for (OnFileDownloadListener l : mListenerList) {
            l.onDownloading(total, current, percent, progress, speed, attachUrl);
        }

//        Log.e(TAG, "下载速度--->"+speed+"  下载进度--->"+percent+"  百分比--->"+progress);

        Notification notification = mNotiMap.get(attachUrl);
        notification.contentView.setProgressBar(R.id.notify_pb, 100, progress, false);
        notification.contentView.setTextViewText(R.id.notify_speed_tv, speed);

        mNotificationManager.notify(mNotiIdMap.get(attachUrl), notification);
    }

    @Override
    public void onDownloadError(int errorType, String attachUrl, Exception e) {
        deleteTask(attachUrl);
//        Log.e(TAG, "download error");
        for (OnFileDownloadListener l : mListenerList) {
            l.onDownloadError(errorType, attachUrl, e);
        }

        Notification notification = mNotiMap.get(attachUrl);
        notification.contentView.setViewVisibility(R.id.notify_completed_tv, View.VISIBLE);
        notification.contentView.setTextViewText(R.id.notify_completed_tv, getString(R.string.pms_notify_download_error));
        notification.contentView.setViewVisibility(R.id.notify_speed_tv, View.GONE);
        notification.contentView.setViewVisibility(R.id.notify_download_iv, View.VISIBLE);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(mNotiIdMap.get(attachUrl), notification);
    }

    @Override
    public void onDownloadCompleted(File file, String attachUrl) {
        deleteTask(attachUrl);
//        Log.e(TAG, attachUrl+"download success");
        for (OnFileDownloadListener l : mListenerList) {
            l.onDownloadCompleted(file, attachUrl);
        }

        Notification notification = mNotiMap.get(attachUrl);
        if (notification == null) {
            return;
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentView.setViewVisibility(R.id.notify_completed_tv, View.VISIBLE);
        notification.contentView.setProgressBar(R.id.notify_pb, 100, 100, false);
        notification.contentView.setTextViewText(R.id.notify_completed_tv, getString(R.string.pms_notify_download_completed));
        notification.contentView.setViewVisibility(R.id.notify_speed_tv, View.GONE);

        mNotificationManager.notify(mNotiIdMap.get(attachUrl), notification);
        mNotiIdMap.remove(attachUrl);
        mNotiMap.remove(attachUrl);

        //记录完成后的文件
        mCompletedFilePath.put(attachUrl, file.getAbsolutePath());
    }

    public static void init(String downloadPath) {
        mDownloadPath = downloadPath;
    }
}
