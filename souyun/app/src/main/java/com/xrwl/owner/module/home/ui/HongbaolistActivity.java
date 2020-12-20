package com.xrwl.owner.module.home.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.module.me.ui.BankyueActivity;
import com.xrwl.owner.module.publish.mvp.OrderConfirmContract;
import com.xrwl.owner.module.publish.mvp.OrderConfirmPresenter;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class HongbaolistActivity extends AppCompatActivity{

    @BindView(R.id.jieguo)
    TextView mjieguo;

    @BindView(R.id.fanhuijian)
    ImageView mfanhuijian;


    @BindView(R.id.money)
    TextView mmoney;

    protected static String renminbi="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hongbaolist);


        Intent intent = getIntent();
        final String orderid=intent.getStringExtra("orderid");

        String nums =  intent.getStringExtra("nums").substring(0,intent.getStringExtra("nums").indexOf("."));
        double b=0;
        int num=Integer.parseInt(nums);
        JSONObject obj = new JSONObject();
        if(num>100){
            double arr[] = {0.8,1.6,1.8,3.6,3.8,16.16};//定义一个数组
            //obj.put(arr, obj);
            obj.toString();
            int randomIndex = (int) (Math.random()*arr.length-1);//生产随机数
            double random = arr[randomIndex];//
            if(random==0.8){
                System.out.println(Math.random() < 0.3 ? 0.8 : random);//如果是0.8，出现的几率为%30
                b=0.8;
            }else if (random==1.6){
                System.out.println(Math.random() < 0.3 ? 1.6 : random);
                b=1.6;
            }else if (random==1.8){
                System.out.println(Math.random() < 0.3 ? 1.8 : random);
            }else if (random==3.6){
                System.out.println(Math.random() < 0.05 ? 3.6 : random);
                b=3.6;
            }else if (random==3.8){
                System.out.println(Math.random() < 0.04 ? 3.8 : random);
                b=3.8;
            }else if (random==16.16){
                System.out.println(Math.random() < 0.01 ? 16.16 : random);
                b=16.16;
            }else{
                b=0.8;
            }

        }else if (num<=100){
            double arr[] = {0.6,0.8,1.06,1.08,1.6,1.8};
            //obj.put(arr, obj);
            obj.toString();
            int randomIndex = (int) (Math.random()*arr.length-1);
            double random = arr[randomIndex];
            if(random==0.6){
                System.out.println(Math.random() < 0.2 ? 0.6 : random);
                b=0.6;
            }else if (random==0.8){
                System.out.println(Math.random() < 0.2 ? 0.8 : random);
                b=0.8;
            }else if (random==1.06){
                System.out.println(Math.random() < 0.2 ? 1.06 : random);
                b=1.06;
            }else if (random==1.08){
                System.out.println(Math.random() < 0.2 ? 1.08 : random);
                b=1.08;
            }else if (random==1.6){
                System.out.println(Math.random() < 0.1 ? 1.6 : random);
                b=1.6;
            }else if (random==1.8){
                System.out.println(Math.random() < 0.1 ? 1.8 : random);
                b=1.8;
            } else
            {
                b=0.6;
            }
        }

       TextView tv = (TextView) findViewById(R.id.money);
        tv.setText(String.valueOf(b));
        renminbi=String.valueOf(b);
        mjieguo = (TextView) findViewById(R.id.jieguo);
        mfanhuijian = (ImageView) findViewById(R.id.fanhuijian);
        //1.匿名内部类
        mjieguo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HongbaolistActivity.this,
                        BankyueActivity.class);
                intent.putExtra("title", "金        额");
                intent.putExtra("pricehongbao", renminbi);
                intent.putExtra("orderid", orderid);
                startActivity(intent);

            }
        });
        //1.匿名内部类
        mfanhuijian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HongbaolistActivity.this, TabActivity.class);
                startActivity(intent);

            }
        });

        initView();
    }
    private void initView() {
    }
}
