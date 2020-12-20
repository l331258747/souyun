package com.ldw.library.utils.filedownload;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.ldw.library.utils.Utils;
import com.ldw.library.utils.fileupload.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by www.longdw.com on 2016/10/26 下午5:17.
 */

public class FileDownloadTask extends AsyncTask<Void, Integer, Void> {

    public static final String TAG = FileDownloadTask.class.getSimpleName();
    private static final long DEFAULT_MILLISECONDS = 10_000L;
    /**
     * 常规下载错误
     */
    private static final int ERROR_TYPE_NORMAL = 1;

    /**
     * 没有SD卡
     */
    private static final int ERROR_TYPE_NOSDCARD = 2;
    /**
     * 存储容量不足
     */
    private static final int ERROR_TYPE_MEMORYFULL = 3;

    private Platform mPlatform = Platform.get();

    private OnFileDownloadListener mListener;

    private String mAttachUrl;
    private String mFileName;
    /**
     * 临时文件，防止清空缓存的时候误删
     */
    private String mTempFile;
    private String mSavedPath;
    private Context mContext;

    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00%");
    private DecimalFormat mSpeedFormat = new DecimalFormat("0.0");
    private Call mCall;

    public FileDownloadTask(Context context, String url, String savedPath, OnFileDownloadListener l) {
        mContext = context;
        mAttachUrl = url;
        mListener = l;
        mFileName = Utils.getFileNameFromUrl(url);
        mTempFile = mFileName.substring(0, mFileName.lastIndexOf(".")) + ".temp";
        mSavedPath = savedPath;
    }

    @Override
    protected Void doInBackground(Void... params) {

        final File tempFile = new File(mSavedPath, mTempFile);
        final File file = new File(mSavedPath, mFileName);

        if (mListener != null) {
            mPlatform.execute(new Runnable() {
                @Override
                public void run() {
                    mListener.onDownloadStart(mAttachUrl);
                }
            });
        }

        //下载之前的一些检查
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (mListener != null) {
                mPlatform.execute(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onDownloadError(ERROR_TYPE_NOSDCARD, mAttachUrl, null);
                    }
                });
            }
            return null;
        }

        OkHttpClient client = new OkHttpClient();
        client.newBuilder()
                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        Request request = new Request.Builder().url(mAttachUrl).build();
        mCall = client.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (mListener != null) {
                    mPlatform.execute(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onDownloadError(ERROR_TYPE_NORMAL, mAttachUrl, e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    final long total = response.body().contentLength();
                    //存储容量不足
                    if (getSDFreeSize() < total) {
                        if (mListener != null) {
                            mPlatform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    mListener.onDownloadError(ERROR_TYPE_MEMORYFULL, mAttachUrl, null);
                                }
                            });
                        }
                        return;
                    }

                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(tempFile);
                    long startTime = System.currentTimeMillis();
                    int startProgress = 0;
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        long intervalTime = (System.currentTimeMillis() - startTime) / 1000;
                        if (intervalTime == 0) {
                            intervalTime = 1;
                        }

                        final int progress = (int) (current * 100 / total);
                        if (progress - startProgress > 1) {//防止频繁更新导致Notification卡死
                            System.out.println("current=" + current + " intervalTime=" + intervalTime);

                            startProgress = progress;

                            double speed = ((double) current / intervalTime);
                            final String speedDes;

                            if (speed < 1024) {//<1KB
                                speedDes = mSpeedFormat.format(speed) + "B/s";
                            } else if (speed < 1024 * 1024) {//< 1M
                                speed = speed / 1024;
                                speedDes = mSpeedFormat.format(speed) + "KB/s";
                            } else {
                                speed = speed / 1024 / 1024;
                                speedDes = mSpeedFormat.format(speed) + "M/s";
                            }

                            if (mListener != null) {
                                final long tempCurrent = current;
                                mPlatform.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        double per = (double) tempCurrent / total;
                                        mListener.onDownloading(total, tempCurrent, mDecimalFormat.format(per), progress,
                                                speedDes,
                                                mAttachUrl);
                                    }
                                });
                            }
                        }
                    }
                    fos.flush();

                    //将临时文件写入到正式文件中
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileInputStream fileIs = new FileInputStream(tempFile);
                    FileOutputStream fileOs = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];

                    int byteread; // 读取的字节数
                    while ((byteread = fileIs.read(buffer)) != -1) {
                        fileOs.write(buffer, 0, byteread);
                    }

                    if (mListener != null) {
                        mPlatform.execute(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onDownloadCompleted(file, mAttachUrl);
                            }
                        });
                    }
                } catch (final Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    tempFile.delete();
                    file.delete();
                    if (mListener != null) {
                        mPlatform.execute(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onDownloadError(ERROR_TYPE_NORMAL, mAttachUrl, e);
                            }
                        });
                    }
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                        tempFile.delete();
                    } catch (final IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                        tempFile.delete();
                        file.delete();
                        if (mListener != null) {
                            mPlatform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    mListener.onDownloadError(ERROR_TYPE_NORMAL, mAttachUrl, e);
                                }
                            });
                        }
                    }

                }
            }
        });

        return null;
    }

    private long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
        } else {
            blockSize = sf.getBlockSize();
        }
        //空闲的数据块的数量
        long freeBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            freeBlocks = sf.getAvailableBlocksLong();
        } else {
            freeBlocks = sf.getAvailableBlocks();
        }
        //返回SD卡空闲大小
        return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
//        return (freeBlocks * blockSize) /1024 /1024; //单位MB
    }

    public String getUrl() {
        return mAttachUrl;
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
