package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.ConvertUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CategoryChildActivity extends BaseActivty {

    public static final String TAG = CategoryChildActivity.class.getName();

    @BindView(R.id.rv_head)
    RelativeLayout rvHead;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    CategoryChildActivity mContext;
    NewGoodAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    int pageId = 1;
    int catId;
    GridLayoutManager glm;
    @BindView(R.id.title_back_imageView)
    ImageView titleBackImageView;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddtime;
//    jiangxu
boolean addTimeAsc=false;
    boolean priceAsc=false;
//
    int sortBy=I.SORT_BY_ADDTIME_DESC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new NewGoodAdapter(mContext, mList);
        catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        L.e(TAG, "catId=======" + catId);
        if (catId == 0) {
            finish();
        }
        super.onCreate(savedInstanceState);
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
                pageId = 1;
                downloadCategoryGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downloadCategoryGoods(final int action) {
        NetDao.downloadCategoryGoods(mContext, catId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
//                处理“刷新中”隐藏
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);

                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addDate(list);
                    }
                    if (list.size() < I.PAGE_ID_DEFAULT) {
                        mAdapter.setMore(false);
                    } else {
                        mAdapter.setMore(true);
                    }
                }
            }

            @Override
            public void onError(String error) {
//                添加工具
                CommonUtils.showShortToast(error);

                L.e("error" + error);

            }
        });
    }

    private void setPullup() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastpostion = glm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastpostion == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageId++;
                    downloadCategoryGoods(I.ACTION_PULL_UP);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
//                int firstPosition = glm.findFirstVisibleItemPosition();
//                srl.setEnabled(firstPosition==0);
            }
        });

    }

    @Override
    protected void initData() {
        downloadCategoryGoods(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void intiView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)

        );

        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        glm.setOrientation(LinearLayoutManager.VERTICAL);
//        是否修饰大小
        rv.setLayoutManager(glm);
        rv.setHasFixedSize(true);
//        mList = new ArrayList<>();
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
//        mAdapter = new NewGoodAdapter(mContext,mList);
    }

    @OnClick(R.id.title_back_imageView)
    public void onClick() {
        MFGT.finish(this);

    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void onClick(View view) {
//        判断
        L.e("sortby、、、、");
        switch (view.getId()) {
            case R.id.btn_sort_price:
                if(priceAsc){
                    sortBy=I.SORT_BY_PRICE_ASC;

                }else{
                    sortBy=I.SORT_BY_PRICE_DESC;

                }
                priceAsc =!priceAsc;

                break;
            case R.id.btn_sort_addtime:
                if(addTimeAsc){
                    sortBy=I.SORT_BY_ADDTIME_ASC;
                }else{
                    sortBy=I.SORT_BY_ADDTIME_DESC;
                }
                addTimeAsc=!addTimeAsc;
                break;
        }
        L.e("sortby....s"+sortBy);
//       自动通知页面去更新
        mAdapter.setSoryBy(sortBy);
    }
}
