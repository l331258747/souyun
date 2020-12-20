package com.xrwl.owner.module.me.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.module.account.activity.ModifyPwdActivity;
import com.xrwl.owner.module.me.bean.Me;
import com.xrwl.owner.utils.AccountUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by www.longdw.com on 2018/3/31 下午3:12.
 */

public class MeHeaderDelegate implements ItemViewDelegate<Me> {

    private Account mAccount;
    private Context mContext;
    @BindView(R.id.yhjpic)
    ImageView myyhjpic;
    @BindView(R.id.yhkpic)
    ImageView myyhkpic;
    @BindView(R.id.yepic)
    ImageView myyepic;

    public MeHeaderDelegate(Context context) {
        mContext = context;
        mAccount = AccountUtil.getAccount(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.me_header_recycler_item;
    }

    @Override
    public boolean isForViewType(Me item, int position) {
        return item.isHeader();
    }
//    @OnClick({R.id.yhjpic, R.id.yhkpic, R.id.yepic})
//    public void OnClick(View v) {
//        if (v.getId() == R.id.yhjpic) {
//            Intent intent = new Intent(mContext, ModifyPwdActivity.class);
//            intent.putExtra("type", 2);
//            intent.putExtra("title", "修改密码");
//            startActivity(intent);
//        }
//        else if(v.getId() == R.id.yhkpic)
//        {
//
//        }
//        else if (v.getId() == R.id.yepic)
//        {
//
//        }
  //  }
    @Override
    public void convert(ViewHolder holder, Me me, int position) {
        String avatar = mAccount.getAvatar();
        ImageView iv = holder.getView(R.id.meItemAvatarIv);
        if (!TextUtils.isEmpty(avatar) && (avatar.startsWith("http://") || avatar.startsWith("https://"))) {
            Glide.with(mContext).load(avatar).into(iv);
        }
        holder.setText(R.id.meItemNameTv, mAccount.getName());
        holder.setText(R.id.meItemPhoneTv, mAccount.getPhone());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
