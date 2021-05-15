package com.xrwl.owner.module.order.owner.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.view.TitleView;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;

public class OrderQrcodeActivity extends BaseActivity {

    @BindView(R.id.baseTitleView)
    TitleView mTitleView;

    @BindView(R.id.view_bg)
    LinearLayout view_bg;
    @BindView(R.id.iv_code)
    ImageView iv_code;
    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.tv_end)
    TextView tv_end;
    @BindView(R.id.tv_type)
    TextView tv_type;

    String orderId;
    String orderStart;//出发地
    String orderEnd;//目的地
    int orderType;

    private ProgressDialog mDialog;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_qrcode;
    }

    @Override
    protected void initViews() {
        orderId = getIntent().getStringExtra("orderId");
        orderType = getIntent().getIntExtra("orderType", 0);
        orderStart = getIntent().getStringExtra("orderStart");
        orderEnd = getIntent().getStringExtra("orderEnd");

        mTitleView.setOnTitleViewListener(new TitleView.TitleViewListener() {
            @Override
            public void onRight() {
                //保存二维码
                aaa();
            }

            @Override
            public void onBack() {
                finish();
            }
        });

        if (orderType == 0) {//出货码
            view_bg.setBackgroundResource(R.drawable.btn_0547aa_r15);
            tv_type.setText("16搜云出发码");
        } else {//到货码
            view_bg.setBackgroundResource(R.drawable.btn_d6a206_r15);
            tv_type.setText("16搜云到达码");
        }

        tv_start.setText("出发地：" + orderStart);
        tv_end.setText("目的地：" + orderEnd);

        createQrcode();

    }

    private void aaa() {
        mTitleView.getRightTv().setClickable(false);
        //相关权限的申请 存储权限
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                mDialog = LoadingProgress.showProgress(mContext, "正在保存图片...");
                saveMyBitmap(Calendar.getInstance().getTimeInMillis() + "", createViewBitmap(view_bg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //权限申请的回调 
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mDialog = LoadingProgress.showProgress(mContext, "正在保存图片...");
                    try {
                        saveMyBitmap(Calendar.getInstance().getTimeInMillis() + "", createViewBitmap(view_bg));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mTitleView.getRightTv().setClickable(true);
                    Toast.makeText(mContext, "请先开启读写权限", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    //使用IO流将bitmap对象存到本地指定文件夹 
    public void saveMyBitmap(final String bitName, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = Environment.getExternalStorageDirectory().getPath();
                File file = new File(filePath + "/DCIM/Camera/" + bitName + ".png");
                try {
                    file.createNewFile();
                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);


                    Message msg = Message.obtain();
                    msg.obj = file.getPath();
                    handler.sendMessage(msg);
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(getApplicationContext()
                        .getContentResolver(), picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + picFile)));
            Toast.makeText(mContext, "图片保存图库成功", Toast.LENGTH_LONG).show();
            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();
            mTitleView.getRightTv().setClickable(true);
        }
    };


    private void createQrcode() {
        Bitmap bitmap = null;
        String content = "www.16souyun.com/h5.aspx?id=" + orderId;
        //生成二维码
        Bitmap logo = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        bitmap = CodeCreator.createQRCode(content, 400, 400, logo);

        if (bitmap != null) {
            iv_code.setImageBitmap(bitmap);
        }
    }


}