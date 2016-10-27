package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartBeanAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.ConvertUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment {


    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    LinearLayoutManager llm;
    MainActivity mContext;
    CartBeanAdapter mAdapter;
    ArrayList<CartBean> mList;

    public CartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
        mAdapter = new CartBeanAdapter(mContext, mList);
//        initView();
//        initData();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initDate() {
        downloadCart(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void setListener() {
        setPulldown();

    }

    private void setPulldown() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setCursorVisible(true);
                downloadCart(I.ACTION_DOWNLOAD);
            }
        });
    }

    private void downloadCart(final int action) {
        User user = FuLiCenterApplication.getUser();
        if(user!=null){
            NetDao.downloadCart(mContext,user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    Log.i("main:", "r="+result);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
//
                    if(result!=null&&result.length>0){
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        Log.i("main:", "s="+list.size());
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

    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)


        );
        llm = new LinearLayoutManager(mContext);
//        是否修饰大小
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        mList = new ArrayList<>();
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));


    }
}
