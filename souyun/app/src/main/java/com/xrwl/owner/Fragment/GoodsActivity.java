package com.xrwl.owner.Fragment;

import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.view.TitleView;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.CompanyFahuoBean;
import com.xrwl.owner.bean.CompanyShouhuoBean;
import com.xrwl.owner.bean.HomeChexingBean;
import com.xrwl.owner.bean.HomeHuowuBean;
import com.xrwl.owner.bean.MarkerBean;
import com.xrwl.owner.module.friend.ui.FriendFragment;

import butterknife.BindView;

public class GoodsActivity extends BaseActivity {

    @BindView(R.id.baseTitleView)
    TitleView baseTitleView;

    public MarkerBean myLocation;//出发地
    public MarkerBean destination;//目的地
    public CompanyFahuoBean fahuodanweiBean;
    public CompanyShouhuoBean shouhuodanweiBean;
    public HomeChexingBean chexing;
    public HomeHuowuBean huowu;

    public int type;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.goods_activity;
    }

    @Override
    protected void initViews() {
        myLocation = (MarkerBean) getIntent().getSerializableExtra("myLocation");
        destination = (MarkerBean) getIntent().getSerializableExtra("destination");
        fahuodanweiBean = (CompanyFahuoBean) getIntent().getSerializableExtra("fahuodanweiBean");
        shouhuodanweiBean = (CompanyShouhuoBean) getIntent().getSerializableExtra("shouhuodanweiBean");
        chexing = (HomeChexingBean) getIntent().getSerializableExtra("chexing");
        huowu = (HomeHuowuBean) getIntent().getSerializableExtra("huowu");

        switch (type){
            case 0:
                baseTitleView.setTitle("大宗运输");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.friendContainerLayout, DzysFragment
                                .newInstance(myLocation,destination,fahuodanweiBean,shouhuodanweiBean,chexing,huowu))
                        .commit();
                break;
            case 1:
                baseTitleView.setTitle("长途零担");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.friendContainerLayout, CtldFragment.newInstance("好友",""))
                        .commit();
                break;
            case 2:
                baseTitleView.setTitle("长途整车");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.friendContainerLayout, CtzcFragment.newInstance("好友", ""))
                        .commit();
                break;
            case 3:
                baseTitleView.setTitle("同城零担");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.friendContainerLayout, TcldFragment.newInstance("好友", ""))
                        .commit();
                break;
            case 4:
                baseTitleView.setTitle("同城专车");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.friendContainerLayout, TczcFragment.newInstance("好友", ""))
                        .commit();
                break;
        }
    }
}
