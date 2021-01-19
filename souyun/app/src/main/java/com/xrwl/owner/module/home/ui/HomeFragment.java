package com.xrwl.owner.module.home.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.Fragment.BlankFragment;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseFragment;
import com.xrwl.owner.module.me.dialog.ExitDialog;
import com.xrwl.owner.module.me.ui.BankActivity;
import com.xrwl.owner.module.me.ui.BankyueActivity;
import com.xrwl.owner.module.me.ui.CouponActivity;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.publish.dialog.CarManageDialog;
import com.xrwl.owner.module.publish.dialog.ProductDialog2;
import com.xrwl.owner.module.publish.ui.AddressActivity;
import com.xrwl.owner.module.publish.ui.ReceiptActivity;
import com.xrwl.owner.module.publish.view.CompanyManageActivity;
import com.xrwl.owner.utils.StatusBarUtil;
import com.xrwl.owner.view.ControlScrollViewPager;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/3/25 下午10:58.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ControlScrollViewPager viewpager;

    @BindView(R.id.wode)
    ImageView wode;
    @BindView(R.id.wode2)
    ImageView wode2;
    @BindView(R.id.sys)
    ImageView sys;
    @BindView(R.id.nav)
    NavigationView nav;
    @BindView(R.id.nidaye)
    DrawerLayout nidaye;
    private ArrayList<String> title_list;
    private ArrayList<Fragment> fragment_list;

    int currentPage;

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

        initNav();

//        Typeface mtypeface=Typeface.createFromAsset(mContext.getAssets(),"fonts/hwxk.ttf");
//        tvTitle.setTypeface(mtypeface);

    }

    private void initNav() {
        nav.setItemIconTintList(null);

        ViewGroup.LayoutParams params = nav.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 5 / 9; //屏幕的三分之一
        nav.setLayoutParams(params);

        View headerView = nav.getHeaderView(0);//获取头布局
        View view_yhj = headerView.findViewById(R.id.view_yhj);
        View view_yhk = headerView.findViewById(R.id.view_yhk);
        View view_ye = headerView.findViewById(R.id.view_ye);
        view_yhj.setOnClickListener(v -> {
            //优惠卷pic
            Intent intent = new Intent(mContext, CouponActivity.class);
            intent.putExtra("title", "优惠卷");
            startActivity(intent);
        });
        view_yhk.setOnClickListener(v -> {
            //银行卡pic
            Intent intent = new Intent(mContext, BankActivity.class);
            intent.putExtra("title", "绑定银行卡");
            startActivity(intent);
        });
        view_ye.setOnClickListener(v -> {
            //鱼额pic
            Intent intent = new Intent(mContext, BankyueActivity.class);
            intent.putExtra("title", "金        额");
            startActivity(intent);
        });


        nav.setNavigationItemSelectedListener(item -> {

            showToast(item.toString().trim());
            if (item.toString().trim().equals("我的消息")) {
                Intent intent = new Intent(mContext, BusinessActivity.class);
                intent.putExtra("title", "我的消息");
                startActivity(intent);
            }
            if (item.toString().trim().equals("我的订单")) {
                Intent intent = new Intent(mContext, OwnerOrderActivity.class);
                intent.putExtra("title", "我的订单");
                startActivity(intent);
            }
            if (item.toString().trim().equals("实名认证")) {
                startActivity(new Intent(getContext(), OwnerAuthActivity.class));
            }
            if (item.toString().trim().equals("地址管理")) {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                intent.putExtra("isItemClick", false);
                startActivity(intent);
            }
            if (item.toString().trim().equals("发票管理")) {
                startActivity(new Intent(getContext(), ReceiptActivity.class));
            }
            if (item.toString().trim().equals("联系客服")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "0357-2591666");
                intent.setData(data);
                startActivity(intent);
            }
            if (item.toString().trim().equals("退出登陆")) {
                ExitDialog dialog = new ExitDialog();
                dialog.show(getFragmentManager(), "exit");
            }
            if (item.toString().trim().equals("车辆管理")) {
                CarManageDialog dialog = new CarManageDialog();
                dialog.show(Objects.requireNonNull(getFragmentManager()), "carManage");
            }
            if (item.toString().trim().equals("大宗地址管理")) {
                startActivity(new Intent(mContext, CompanyManageActivity.class));
            }
            if (item.toString().trim().equals("大宗名称管理")) {
                ProductDialog2 dialog = new ProductDialog2();
                dialog.show(Objects.requireNonNull(getFragmentManager()), "product");
            }
            nidaye.closeDrawer(nav);
            return true;

        });
    }


    private void loadData() {
        title_list.add("指尖上的发货平台");
//        title_list.add("大宗运输");
//        title_list.add("长途零担");
//        title_list.add("长途整车");
//        title_list.add("同城零担");
//        title_list.add("同城专车");
//        title_list.add("跑腿");


        BlankFragment f = new BlankFragment();
        fragment_list.add(f);
//        DzysFragment twoFragment4 = new DzysFragment();
//        fragment_list.add(twoFragment4);
//        CtldFragment twoFragment6 = new CtldFragment();
//        fragment_list.add(twoFragment6);
//        CtzcFragment twoFragment5 = new CtzcFragment();
//        fragment_list.add(twoFragment5);
//        TcldFragment twoFragment = new TcldFragment();
//        fragment_list.add(twoFragment);
//        TczcFragment twoFragment3 = new TczcFragment();
//        fragment_list.add(twoFragment3);
//        PaotuiFragment paotuiFragment = new PaotuiFragment();
//        fragment_list.add(paotuiFragment);

    }

    private void showView() {
        //getChildFragmentManager 要不然子fragment 通过 getparentfragment 找不到
        viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        viewpager.setOffscreenPageLimit(fragment_list.size());

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                setTitle(title_list.get(currentPage));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setTabIndex(int i) {
        viewpager.setCurrentItem(i);
        currentPage = i;
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void z1() {
//        Window window = this.getActivity().getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(this.getResources().getColor(R.color.zbb));

//        Eyes.setStatusBarColor(this.getActivity(),this.getResources().getColor(R.color.zbb));

        StatusBarUtil.setStatusBar(this.getActivity(), getResources().getColor(R.color.colorPrimaryDark));
    }

    @OnClick({R.id.wode,R.id.wode2, R.id.sys})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wode:
            case R.id.wode2:
                if (nidaye.isDrawerOpen(nav)) {
                    nidaye.closeDrawer(nav);
                } else {
                    nidaye.openDrawer(nav);
                }
                break;
            case R.id.sys:
//                Intent intent = new Intent(getContext(), NearLocationActivity.class);
//                startActivity(intent);

                startActivity(new Intent(mContext, saoyisaoActivity.class));

                break;
        }
    }

    public boolean onBackPressed(){
        if(nidaye.isDrawerOpen(nav)){
            nidaye.closeDrawer(nav);
            return true;
        }
        return false;
    }
}
