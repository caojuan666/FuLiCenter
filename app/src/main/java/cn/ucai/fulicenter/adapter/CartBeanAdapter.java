package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;


public class CartBeanAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;

    private int footerString;
    boolean isMore;

    public CartBeanAdapter(Context context, ArrayList<CartBean> list) {
        mContext = context;
        mList = list;
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
        final CartBean cartBean = mList.get(position);
        GoodsDetailsBean goods = cartBean.getGoods();
        if (goods != null) {
            ImageLoader.downloadImg(mContext, ((CartViewHolder) holder).ivGoods, goods.getGoodsThumb());
            ((CartViewHolder) holder).tvGoodsName.setText(goods.getGoodsName());
            ((CartViewHolder) holder).tvGoodsPrice.setText(goods.getCurrencyPrice());
            ((CartViewHolder) holder).cbCartSelect.setChecked(cartBean.isChecked());
            ((CartViewHolder) holder).cbCartSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    cartBean.setChecked(b);
//                    发送广播
                    mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CATR));
                }
            });
            ((CartViewHolder) holder).ivAddCount.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {*/
        return I.TYPE_ITEM;
        //}
    }

    public int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    public void setMore(boolean b) {

    }

    public void initData(ArrayList<CartBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addDate(ArrayList<CartBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }


    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_cart_Select)
        CheckBox cbCartSelect;
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
            @OnClick(R.id.iv_add_Count)
             public   void addCart(){
            final int position = (int) ivAddCount.getTag();
                CartBean cart = mList.get(position);
                NetDao.updateCart(mContext, cart.getId(), cart.getCount()+1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null){
                            mList.get(position).setCount(mList.get(position).getCount()+1);
                            mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CATR));
                            tvGoodsCounts.setText(String.valueOf(mList.get(position).getCount()));

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });


            }
        @OnClick(R.id.iv_delete_counts)
        public   void delCart(){
            final int position = (int) ivAddCount.getTag();
            CartBean cart = mList.get(position);
            if(cart.getCount()>1) {


                NetDao.updateCart(mContext, cart.getId(), cart.getCount()-1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null) {
                            mList.get(position).setCount(mList.get(position).getCount() - 1);
                            mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CATR));
                            tvGoodsCounts.setText(String.valueOf(mList.get(position).getCount()));

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else {
                    NetDao.delCart(mContext, cart.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if(result!=null&&result.isSuccess()){
                                mList.remove(position);
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CATR));
                                notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
            }

        }
    }
}
