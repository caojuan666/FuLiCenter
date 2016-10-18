package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
//适配器
public class NewGoodAdapter extends Adapter {
    TextView mTvDes, mTvSprice;
    Context mContext;
    List<NewGoodsBean> mList;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    boolean isMore;

    public NewGoodAdapter(Context context, List<NewGoodsBean> list) {
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
            holder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_newgoods, null));
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
            GoodsViewHolder v = (GoodsViewHolder) holder;
            NewGoodsBean goods = mList.get(position);
           ImageLoader.downloadImg(mContext,v.imPic,goods.getGoodsThumb());
            v.tvDes.setText(goods.getGoodsName());
            v.tvSprice.setText(goods.getCurrencyPrice());

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
    public void initData(ArrayList<NewGoodsBean> list) {
        if(mList!=null){
            this.mList.clear();

        }
        this.mList.addAll(list);
//        刷新
        notifyDataSetChanged();


    }
    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imPic)
        ImageView imPic;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tvSprice)
        TextView tvSprice;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FooterViewHolder  extends RecyclerView.ViewHolder{
        @BindView(R.id.tvfooter)
        TextView tvfooter;

        FooterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }
}
