package cn.ucai.fulicenter.fragment;


import android.animation.StateListAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.NewGoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.ConvertUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends BaseFragment {
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    MainActivity mContext;
    NewGoodAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
        int pageId = 1;
    GridLayoutManager glm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        super.onCreateView(inflater, container, savedInstanceState);
//        initView();
//        initDate();
//        setListener();
        return layout;
    }
    @Override
    protected void setListener() {
        setPullup();
        setPulldown();
    }

    private void setPulldown() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
               tvRefresh.setCursorVisible(true);
                downloadNewGoods(I.ACTION_PULL_DOWN);
            }
        });
}

    private void downloadNewGoods(final int action) {
        NetDao.downloadNewGoods(mContext, pageId,new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>(){
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
//                处理“刷新中”隐藏
                        srl.setRefreshing(false);
                        tvRefresh.setVisibility(View.GONE);

                        if(result!=null&&result.length>0){
                            ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                            if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                                mAdapter.initData(list);
                            }else{
                                mAdapter.addDate(list);
                            }
                            if(list.size()<I.PAGE_ID_DEFAULT){
                                mAdapter.setMore(false);
                            }else {
                                mAdapter.setMore(true);
                            }
                        }
                    }
                    @Override
                    public void onError(String error) {
//                添加工具
                        CommonUtils.showShortToast(error);

                        L.e("error"+error);

                    }
                });
    }
    private void setPullup() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
             int lastpostion=   glm.findLastVisibleItemPosition();
           if(newState==RecyclerView.SCROLL_STATE_IDLE
                   &&lastpostion==mAdapter.getItemCount()-1
                   &&mAdapter.isMore()){
               pageId++;
               downloadNewGoods(I.ACTION_PULL_UP);

           }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    protected void initDate() {
        downloadNewGoods(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)

        );
        mContext= (MainActivity) getContext();
         glm = new GridLayoutManager(mContext, I.COLUM_NUM);
//        是否修饰大小
        rv.setLayoutManager(glm);
        rv.setHasFixedSize(true);
        mList = new ArrayList<>();
        mAdapter = new NewGoodAdapter(mContext,mList);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
    }
}




