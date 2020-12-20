package com.ldw.library.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ldw.library.R;

/**
 * Created by www.longdw.com on 16/9/19 下午1:38.
 */
public class LoadingProgress {

    public static android.app.ProgressDialog showProgress(Context context, String msg) {
        return showProgress(context, msg, false);
    }
    public static android.app.ProgressDialog showProgress(Context context, String msg, boolean cancelable) {
        android.app.ProgressDialog pd = new android.app.ProgressDialog(context, R.style.CustomDialogStyle);
        pd.show();

        View view = View.inflate(context, R.layout.ul_loading_dialog_layout, null);
        pd.setContentView(view);

        TextView tv = (TextView) view.findViewById(R.id.custom_progress_tv);
        if (!TextUtils.isEmpty(msg)) {
            tv.setText(msg);
        }

        pd.setCancelable(cancelable);
        pd.setCanceledOnTouchOutside(false);

        return pd;
    }

}
