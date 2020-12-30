package com.xrwl.owner.module.order.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xrwl.owner.R;

public class EditDialog extends Dialog {

    Context mContext;

    TextView btn_cancel,btn_submit,tv_title;

    EditText et_content;

    public EditDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_edit, null);
        this.setContentView(layout);

        btn_cancel = layout.findViewById(R.id.btn_cancel);
        btn_submit = layout.findViewById(R.id.btn_submit);
        et_content = layout.findViewById(R.id.et_content);
        tv_title = layout.findViewById(R.id.tv_title);



        if(!TextUtils.isEmpty(title))
            tv_title.setText(title);

        if(!TextUtils.isEmpty(content)){
            et_content.setText(content);
            et_content.setSelection(content.length());
        }


        btn_submit.setOnClickListener(view -> {
            if(submitListener != null){
                submitListener.onClick(EditDialog.this,et_content.getText().toString());
            }
//                dismiss();
        });


        btn_cancel.setOnClickListener(view -> {
            if(cancelListener != null){
                cancelListener.onClick(view);
            }
            dismiss();
        });
    }

    public String getEditContent(){
        return et_content.getText().toString();
    }

    String title;
    public EditDialog setTitle(String title){
        this.title = title;
        return this;
    }

    String content;
    public EditDialog setContent(String content){
        this.content = content;
        return this;
    }

    View.OnClickListener cancelListener;
    public EditDialog setCancelListener(View.OnClickListener cancelListener){
        this.cancelListener = cancelListener;
        return this;
    }

    public interface OnItemClickListener {
        void onClick(Dialog dialog, String content);
    }

    OnItemClickListener submitListener;
    public EditDialog setSubmitListener(OnItemClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }

}
