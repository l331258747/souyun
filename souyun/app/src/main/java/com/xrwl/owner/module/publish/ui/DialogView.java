package com.xrwl.owner.module.publish.ui;
import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.xrwl.owner.R;
public class DialogView {
    private Dialog dialog;

    public DialogView(Activity mActivity) {
        dialog = new Dialog(mActivity, R.style.mask_dialog);
        LinearLayout popView = (LinearLayout) LayoutInflater.
                from(mActivity).inflate(R.layout.dialog_view, null);

        // 关闭按钮
        ImageView viewClose = (ImageView) popView.findViewById(R.id.win_close);
        viewClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        dialog.setContentView(popView,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.dismiss();
    }
}