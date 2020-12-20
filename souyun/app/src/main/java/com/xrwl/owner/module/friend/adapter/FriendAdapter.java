package com.xrwl.owner.module.friend.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.widget.SectionIndexer;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2017/12/13 下午4:10.
 */

public class FriendAdapter extends MultiItemTypeAdapter<EntityWrapper<Friend>> implements SectionIndexer {

    public static final String TAG = FriendAdapter.class.getSimpleName();

    private ArrayList<Integer> mSectionPositions;

    private SparseIntArray mPositionSectionMap = new SparseIntArray();

    /**
     *
     * @param context
     * @param datas
     * @param l
     * @param isAdd  是否是好友添加页面 如果是没有搜索
     */
    public FriendAdapter(Context context, List<EntityWrapper<Friend>> datas, boolean isAdd, FriendSearchDelegate
            .OnAddressSearchListener l, FriendDelegate.OnFriendInviteListener listener) {
        super(context, datas);

        if (isAdd) {
            addItemViewDelegate(new FriendAddHeaderDelegate());
        }

        if (!isAdd) {
            addItemViewDelegate(new FriendSearchDelegate(l));
        }
        addItemViewDelegate(new FriendDelegate(isAdd, listener));
        addItemViewDelegate(new FriendTitleDelegate());
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>();
        mSectionPositions = new ArrayList<>();
        int sectionPos = 0;
        for (int i = 0, size = this.getDatas().size(); i < size; i++) {
            String sectionName = String.valueOf(this.getDatas().get(i).getIndexTitle().charAt(0)).toUpperCase();
            if (!sections.contains(sectionName)) {
                sections.add(sectionName);
                //记录section第一次出现的位置
                mSectionPositions.add(i);
                sectionPos = mSectionPositions.size() - 1;
            }

            mPositionSectionMap.put(i, sectionPos);
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return mPositionSectionMap.get(position);
    }
}
