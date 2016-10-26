package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
//适配器
public class CollectsAdapter extends Adapter {
    Context mContext;
    TextView mTvDes, mTvSprice;
    List<CollectBean> mList;
    int soryBy=I.SORT_BY_ADDTIME_DESC;//降序的依据
    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }
//
    public void setSoryBy(int soryBy) {
        this.soryBy = soryBy;
//        更新页面
        notifyDataSetChanged();
    }

    boolean isMore;

    public CollectsAdapter(Context context, List<CollectBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);


    }

    //创建
//
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new CollectsViewHolder(View.inflate(mContext, R.layout.item_collects, null));
        }
        return holder;
    }
    //绑定商品的名字与价格（访问数据）
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
        FooterViewHolder  v = (FooterViewHolder) holder;
            v.tvfooter.setText(getFooterString());
        } else {
            CollectsViewHolder v = (CollectsViewHolder) holder;

            CollectBean goods = mList.get(position);
            ImageLoader.downloadImg(mContext,v.imPic,goods.getGoodsThumb());
            v.tvDes.setText(goods.getGoodsName());
            v.layout1.setTag(goods);

        }
    }
    private int getFooterString() {

        return isMore?R.string.load_more:R.string.no_more;
    }
    //返回
    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }
    //
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;

        } else {
            return I.TYPE_ITEM;
        }
//        return super.getItemViewType(position);
    }
    public void initData(ArrayList<CollectBean> list) {
        if(mList!=null){
            mList.clear();
        }
        mList.addAll(list);
//        刷新
        notifyDataSetChanged();

    }

    public void addDate(ArrayList<CollectBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
     class CollectsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imPic)
        ImageView imPic;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tv_del)
        ImageView tv_del;
        @BindView(R.id.layout1)
        RelativeLayout layout1;

         CollectsViewHolder(View view) {
             super(view);
             ButterKnife.bind(this, view);
         }
//
        @OnClick(R.id.layout1)
        public void onGoodsItemClick(){
            CollectBean goods = (CollectBean) layout1.getTag();
            MFGT.gotoDetailsActivity(mContext,goods.getGoodsId());

        }
//刪除收藏的商品
         @OnClick(R.id.tv_del)
         public  void  deleteCollects(){
             final CollectBean goods = (CollectBean) layout1.getTag();
             String userame = FuLiCenterApplication.getUser().getMuserName();
             NetDao.deleteCollects(mContext, userame, goods.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                 @Override
                 public void onSuccess(MessageBean result) {
//                     如果收藏不為空就刪除并刷新
                     if(result!=null&&result.isSuccess()){
                         mList.remove(goods);
                         notifyDataSetChanged();
//                         否則為空或這刪除失敗
                     }else {
                         CommonUtils.showLongToast(result != null ? result.getMsg() : mContext.getResources().getString(R.string.delete_collect_fail));
                     }

                 }

                 @Override
                 public void onError(String error) {
                    L.e("error="+error);
                     CommonUtils.showLongToast(mContext.getResources().getString(R.string.delete_collect_fail));

                 }
             });
         }


    }

}

