package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.view.FooterViewHolder;


public class CartBeanAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;
    private int foooterString;
    boolean isMore;

    public CartBeanAdapter(Context context, ArrayList<CartBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
            holder = new CartViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_cart, parent, false));
        return holder;
    }
    //
    @Override

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder c = (CartViewHolder) holder;
        CartBean cartBean = mList.get(position);
        GoodsDetailsBean goods = cartBean.getGoods();
        if (goods!=null) {
            ImageLoader.downloadImg(mContext,((CartViewHolder) holder).ivGoods,goods.getGoodsThumb());
            ((CartViewHolder) holder).tvGoodsName.setText(goods.getGoodsName());
            ((CartViewHolder) holder).tvGoodsPrice.setText(goods.getCurrencyPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size()  : 0;
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {*/
            return I.TYPE_ITEM;
        //}
    }

    public int getFoooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    public void setMore(boolean b) {

    }

    public void initData(ArrayList<CartBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();

    }

    public void addDate(ArrayList<CartBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }




     class CartViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_choose)
        ImageView ivChoose;
        @BindView(R.id.iv_goods)
        ImageView ivGoods;
        @BindView(R.id.tv_goodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.iv_add_Count)
        ImageView ivAddCount;
        @BindView(R.id.tv_goods_counts)
        TextView tvGoodsCounts;
        @BindView(R.id.iv_delete_counts)
        ImageView ivDeleteCounts;

         CartViewHolder(View view) {
             super(view);
             ButterKnife.bind(this, view);
        }
    }
}
