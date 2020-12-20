package com.xrwl.owner.module.publish.dialog;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/15 下午10:19.
 */
public class RemarkDialog extends BasePopDialog {

    private LayoutInflater mInflater;

    @BindView(R.id.prDialogFlowLayout)
    TagFlowLayout mFlowLayout;

    @BindView(R.id.prDialogDateTv)
    TextView mDateTv;

    @BindView(R.id.prDialogRemarkEt)
    EditText mRemarkEt;
    private Calendar mCalendar;
    private List<String> mRemarkDatas;
    private OnRemarkConfirmListener mListener;


    @Override
    protected int getLayoutId() {
        return R.layout.publish_remark_dialog_layout;
    }

    @Override
    protected void initView() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        mDateTv.setText(simpleDateFormat.format(date));


        mCalendar = Calendar.getInstance();
        mInflater = LayoutInflater.from(getContext());

        mRemarkDatas = Arrays.asList(getResources().getStringArray(R.array.publishRemarkTag));

        mFlowLayout.setAdapter(new TagAdapter<String>(mRemarkDatas) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {

                TextView tv = (TextView) mInflater.inflate(R.layout.publish_remark_dialog_tag_item, null);
                tv.setText(s);

                return tv;
            }
        });
    }

    @OnClick({ R.id.prDialogCancelTv, R.id.prDialogConfirmTv ,R.id.prDialogDateTv})
    public void onClick(View v) {
        if (v.getId() == R.id.prDialogCancelTv) {
            dismiss();
        } else if (v.getId() == R.id.prDialogConfirmTv) {
            String date = mDateTv.getText().toString();
            String remark = mRemarkEt.getText().toString();
            Set<Integer> selected = mFlowLayout.getSelectedList();
            StringBuffer tagSb = new StringBuffer();
            int i = 0;
            for (int pos : selected) {
                tagSb.append(mRemarkDatas.get(pos));
                if (i < selected.size() - 1) {
                    tagSb.append(",");
                }
                i++;
            }

            String content = "";
            if (!TextUtils.isEmpty(date)) {
                content += date;
            }
            if (!TextUtils.isEmpty(remark)) {
                content += "  ";
                content += remark;
            }

            if (tagSb.toString().length() > 0) {
                content += "  ";
                content += tagSb.toString();
            }

            if (mListener != null) {
                mListener.onRemarkConfirm(content);
            }

            dismiss();

        } else if (v.getId() == R.id.prDialogDateTv) {
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DATE);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mDateTv.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }, year, month, day);
            dialog.show();
        }
    }

    public void setOnRemarkConfirmListener(OnRemarkConfirmListener l) {
        mListener = l;
    }

    public interface OnRemarkConfirmListener {
        void onRemarkConfirm(String content);
    }
}
