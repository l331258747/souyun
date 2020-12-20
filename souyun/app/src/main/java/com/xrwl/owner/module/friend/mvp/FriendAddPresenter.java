package com.xrwl.owner.module.friend.mvp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.retrofit.BaseSimpleObserver;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/3/28 下午10:23.
 */

public class FriendAddPresenter extends FriendAddContract.AAddPresenter {

    private FriendAddContract.IAddModel mModel;

    private Account mAccount;

    public FriendAddPresenter(Context context) {
        super(context);

        mModel = new FriendAddModel();
    }

    @Override
    public void requestAccounts() {

        Observable.create(new ObservableOnSubscribe<List<EntityWrapper<Friend>>>() {
            @Override
            public void subscribe(ObservableEmitter<List<EntityWrapper<Friend>>> e) throws Exception {

                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String results[] = {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                };
                Cursor cursor = null;
                try {
                    cursor = mContext.getContentResolver().query(uri, results, null, null, null);
                    if (cursor != null) {
                        List<Friend> datas = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Friend f = new Friend();
                            f.name = displayName;
                            f.phone = number;
                            datas.add(f);
                        }

                        //组装数据
                        //转换List
                        List<EntityWrapper<Friend>> result = Friend.transform(datas);
//                        //先返回出去给搜索页面使用
//                        mView.onSearchData((ArrayList<Friend>) datas);
                        e.onNext(result);
                    }
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

            }
        }).compose(RxSchedulers.<List<EntityWrapper<Friend>>>compose())
                .subscribe(new BaseSimpleObserver<List<EntityWrapper<Friend>>>() {
            @Override
            protected void onHandleSuccess(List<EntityWrapper<Friend>> entity) {

//                EntityWrapper<Friend> search = new EntityWrapper<>();
//                search.setIndexTitle("↑");
//                search.setItemType(EntityWrapper.TYPE_HEADER);
//                entity.add(0, search);

                //添加头部
                EntityWrapper<Friend> header = new EntityWrapper<>();
                Friend headerFriend = new Friend();
                header.setData(headerFriend);
                header.setItemType(EntityWrapper.TYPE_HEADER);
                header.setIndexTitle("↑");
                entity.add(0, header);

                BaseEntity<List<EntityWrapper<Friend>>> result = new BaseEntity<>();
                result.setData(entity);

                mView.onRefreshSuccess(result);
            }

            @Override
            protected void onHandleError(Throwable e) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        });
    }

    @Override
    public void postAdd(final Friend friend) {
        Map<String, String> params = new HashMap<>();
        params.put("name", friend.getName());
        params.put("userid", mAccount != null ? mAccount.getId() : (mAccount = getAccount()).getId());
        params.put("tel", friend.getPhone());
        params.put("type", "0");
        mModel.postAdd(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    //这里返回friend，目的是多选循环提交的时候 如果成功了选中的会取消选中，失败了选中还在
                    mView.onPostSuccess(entity, friend);
                } else {
                    mView.onPostError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onPostError(e);
            }
        });
    }
}
