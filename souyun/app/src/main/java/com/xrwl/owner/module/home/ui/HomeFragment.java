package com.xrwl.owner.module.home.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.Fragment.BlankFragment;
import com.xrwl.owner.Fragment.CtldFragment;
import com.xrwl.owner.Fragment.CtzcFragment;
import com.xrwl.owner.Fragment.DzysFragment;
import com.xrwl.owner.Fragment.PaotuiFragment;
import com.xrwl.owner.Fragment.TcldFragment;
import com.xrwl.owner.Fragment.TczcFragment;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseFragment;
import com.xrwl.owner.module.me.dialog.ExitDialog;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.publish.ui.AddressActivity;
import com.xrwl.owner.module.publish.ui.ReceiptActivity;
import com.xrwl.owner.utils.StatusBarUtil;
import com.xrwl.owner.view.ControlScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by www.longdw.com on 2018/3/25 下午10:58.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.tab_layout)
    TextView tabLayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ControlScrollViewPager viewpager;

    Unbinder unbinder;
    @BindView(R.id.wode)
    ImageView wode;
    @BindView(R.id.sys)
    ImageView sys;
    @BindView(R.id.nav)
    NavigationView nav;
    @BindView(R.id.nidaye)
    DrawerLayout nidaye;
    private ArrayList<String> title_list;
    private ArrayList<Fragment> fragment_list;


    public static HomeFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView(View view) {
        z1();
        //设置标题的集合
        title_list = new ArrayList<>();
        //设置显示fragment的集合
        fragment_list = new ArrayList<>();
        //自拟数据
        loadData();
        //关联到一起
        showView();


    }


    private void loadData() {
        title_list.add("主页");
        title_list.add("大宗运输");
        title_list.add("长途零担");
        title_list.add("长途整车");
        title_list.add("同城专车");
        title_list.add("同城零担");
        title_list.add("跑腿");


        BlankFragment f = new BlankFragment();
        fragment_list.add(f);
        DzysFragment twoFragment4 = new DzysFragment();
        fragment_list.add(twoFragment4);
        CtldFragment twoFragment6 = new CtldFragment();
        fragment_list.add(twoFragment6);
        CtzcFragment twoFragment5 = new CtzcFragment();
        fragment_list.add(twoFragment5);
        TczcFragment twoFragment3 = new TczcFragment();
        fragment_list.add(twoFragment3);
        TcldFragment twoFragment = new TcldFragment();
        fragment_list.add(twoFragment);
        PaotuiFragment paotuiFragment = new PaotuiFragment();
        fragment_list.add(paotuiFragment);


    }

    private void showView() {
        viewpager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragment_list.get(position);
            }

            @Override
            public int getCount() {
                return fragment_list.size();
            }

            /**
             * 重写这个方法可以设置标签的值
             */
            @Override
            public CharSequence getPageTitle(int position) {
                return title_list.get(position);
            }
        });
        //关联到一起
        tablayout.setupWithViewPager(viewpager);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        View headerView = nav.getHeaderView(0);//获取头布局

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                showToast(item.toString());

                if (item.toString().contains("我的订单")) {
                    Intent intent = new Intent(mContext, OwnerOrderActivity.class);
                    intent.putExtra("title", "我的订单");
                    startActivity(intent);
                }
                if (item.toString().contains("实名认证")) {
                    startActivity(new Intent(getContext(), OwnerAuthActivity.class));
                }
                if (item.toString().contains("地址管理")) {
                    startActivity(new Intent(getContext(), AddressActivity.class));
                }
                if (item.toString().contains("发票管理")) {
                    startActivity(new Intent(getContext(), ReceiptActivity.class));
                }
                if (item.toString().contains("联系客服")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + "0357-2591666");
                    intent.setData(data);
                    startActivity(intent);
                }
                if (item.toString().contains("退出登录")) {
                    ExitDialog dialog = new ExitDialog();
                    dialog.show(getFragmentManager(), "exit");
                }
                nidaye.closeDrawer(nav);


//                Log.d(item.toString(),"00011");

                return true;

            }
        });


        viewpager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void z1() {
//        Window window = this.getActivity().getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(this.getResources().getColor(R.color.zbb));

//        Eyes.setStatusBarColor(this.getActivity(),this.getResources().getColor(R.color.zbb));

        StatusBarUtil.setStatusBar(this.getActivity(), getResources().getColor(R.color.zbb));
    }

    @OnClick({R.id.wode, R.id.sys})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wode:
                if (nidaye.isDrawerOpen(nav)) {
                    nidaye.closeDrawer(nav);
                } else {
                    nidaye.openDrawer(nav);
                }


                break;
            case R.id.sys:
                Intent intent = new Intent(getContext(), NearLocationActivity.class);
                startActivity(intent);
                break;
        }
    }


}
