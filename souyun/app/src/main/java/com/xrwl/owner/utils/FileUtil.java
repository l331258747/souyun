package com.xrwl.owner.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.xrwl.owner.BuildConfig;

import java.io.File;

/**
 * Created by www.longdw.com on 2018/3/25 下午1:14.
 */

public class FileUtil {
    public static void checkOpenFile(Context context, String filepath) {
        if (TextUtils.isEmpty(filepath)) {
            return;
        }
        File file = new File(filepath);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            if (filepath.endsWith(".apk")) {
                installApk(context, filepath);
            } else {// 不是wps文件则让用户选择w
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                String type = getMIMEType(file);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                } else {
                    uri = Uri.fromFile(file);
                }

                intent.setDataAndType(uri, type);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 识别文件的类型
     *
     * @param f
     * @return
     */
    private static String getMIMEType(File f) {
        String end = f
                .getName()
                .substring(f.getName().lastIndexOf(".") + 1,
                        f.getName().length()).toLowerCase();
        String type;
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
                || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg")) {
            type = "image";
        } else if (end.equals("doc") || end.equals("docx") || end.equals("pdf")
                || end.equals("txt")) {
            type = "application/msword";
            return type;
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    public static void installApk(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } else {
            contentUri = Uri.fromFile(file);

        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

            public static String getFileNameWithUrl(String url) {
                if (url == null) {
                    return null;
        }

        int pos =  url.lastIndexOf("/");
        return url.substring(pos + 1);
    }
}
