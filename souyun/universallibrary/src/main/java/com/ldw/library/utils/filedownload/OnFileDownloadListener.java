package com.ldw.library.utils.filedownload;

import java.io.File;

public interface OnFileDownloadListener {
    void onDownloadStart(String attachUrl);

    void onDownloading(long total, long current, String percent, int progress, String speed, String attachUrl);

    void onDownloadError(int errorType, String attachUrl, Exception e);

    void onDownloadCompleted(File file, String attachUrl);

//    /** 文件已经存在 不需要下载 */
//    void onFileExists(File file, String attachUrl);
}