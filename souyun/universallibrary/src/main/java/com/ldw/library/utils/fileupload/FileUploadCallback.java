package com.ldw.library.utils.fileupload;

import org.json.JSONObject;

/**
 * 附件上传 回调
 * Created by www.longdw.com on 16/10/10 下午3:56.
 */

public abstract class FileUploadCallback {

    public void onSuccess(JSONObject json) {
    }

    public void onError(Exception e) {
    }

    public void inProgress(float progress, long total) {
    }

}
