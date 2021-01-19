package com.xrwl.owner.module.order.owner.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.base.adapter.BasePagerAdapter;
import com.xrwl.owner.bean.BaseMenu;
import com.xrwl.owner.module.tab.activity.TabActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的订单
 * Created by www.longdw.com on 2018/4/3 下午10:49.
 */

public class OwnerOrderActivity extends BaseActivity {

    @BindView(R.id.driverOrderMagicIndicator)
    MagicIndicator mMagicIndicator;

    @BindView(R.id.driverOrderViewPager)
    ViewPager mViewPager;


    @BindView(R.id.fanhuijian)
    ImageView mfanhuijian;
    private ImageView bt;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ownerorder_activity;
    }

    @Override
    protected void initViews() {

        bt = findViewById(R.id.fanhuijian);
        bt.setOnClickListener(view -> {
            Intent intent=new Intent(mContext, TabActivity.class);
            startActivity(intent);
        });

        final List<BaseMenu> datas = new ArrayList<>();
        datas.add(new BaseMenu("未接单", "0"));
        datas.add(new BaseMenu("已接单", "1"));
        datas.add(new BaseMenu("运输中", "2"));
        datas.add(new BaseMenu("已完成", "3"));
        initViewPager(datas);

        CommonNavigator navigator = new CommonNavigator(mContext);
        navigator.setAdjustMode(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return datas == null ? 0 : datas.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(datas.get(index).getName());
                simplePagerTitleView.setTextSize(15);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.dark_gray));//616161
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.indicator_color));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(getResources().getColor(R.color.indicator_color));
                return linePagerIndicator;
            }
        });


        mMagicIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

        int position = getIntent().getIntExtra("position", 0);
        mViewPager.setCurrentItem(position);



        showContent();
    }

    private void initViewPager(List<BaseMenu> list) {
        List<Fragment> fragments = new ArrayList<>();
        for (BaseMenu bm : list) {
            fragments.add(OwnerOrderFragment.newInstance(bm.getId()));
        }

        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(list.size());
    }
}
