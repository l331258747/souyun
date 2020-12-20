package com.xrwl.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xrwl.owner.Frame.auxiliary.IntentUtil;
import com.xrwl.owner.module.loading.activity.LoadingActivity;
import com.xrwl.owner.module.tab.activity.TabActivity;

import static com.blankj.utilcode.util.CrashUtils.init;

public class Main3Activity extends AppCompatActivity {

    private LinearLayout viewById;
    private PopupWindow popupWindow3;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewById = findViewById(R.id.ww);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        preferences = getSharedPreferences("guideActivity", MODE_PRIVATE);
        // 判断是不是首次登录
        if (preferences.getBoolean("firstStart", true)) {
            editor = preferences.edit();
            // 将登录标志位设置为false，下次登录时不在显示引导页
            editor.putBoolean("firstStart", false);
            setContentView(R.layout.activity_main3);
            editor.commit();
            chu3();
            bgAlpha(0.9f);
            // 弹出PopupWindow的具体代码
            // 跳转到引导页

        } else {
//            //跳转到引导页
            Intent intent = new Intent();
            intent.setClass(this, LoadingActivity.class);
            startActivity(intent);
            finish();

        }

    }

    private void chu3() {
        /**1,第一行代码即使为popupwindow创建一个视图,不必多说
         *2,第二行代码创建一个popupwindow,设置它的宽高 为自适应
         * 4,popupwindow是否可以响应外部的点击事件
         * 5popupwindow是否可以响应n内部的点击事件
         * ,popupwindow是否可以相应点击事件\注意:我们为popupwindow设置背景并非是我们需要这个背景,其实一般情况我们的布局文件都会有一个背景的,这是因为当我们设置了setOutsideTouchable的时候我们以为点击外部的区域,popupwindow可以消失,其实不然,因为api的本身bug问题,我们必须为其设置一个背景,但是为了不影响正常的背景使用,所以推荐设置成透明背景
         */
        View contentView = LayoutInflater.from(Main3Activity.this).inflate(R.layout.item, null);
        popupWindow3 = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow3.setOutsideTouchable(true);
        popupWindow3.setTouchable(true);
        popupWindow3.setFocusable(false);
        popupWindow3.setOutsideTouchable(false);
        //这是展示popupWindow的 方法  也可在具体事件里写最好
//        popupWindow3.showAtLocation(viewById, Gravity., 0, 100);

        popupWindow3.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);


        TextView qd = contentView.findViewById(R.id.qd);
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow3.dismiss();
                bgAlpha(1f);
                IntentUtil.get().goActivity(Main3Activity.this, LoadingActivity.class);
            }
        });


    }


    /**
     * popupwindow 外部变暗方法
     */
    private void bgAlpha(float f) {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.alpha = f;
        this.getWindow().setAttributes(layoutParams);
    }


}
