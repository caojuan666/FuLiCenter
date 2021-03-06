package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FooterViewHolder;

import static cn.ucai.fulicenter.R.id.rv;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
//适配器
public class NewGoodAdapter extends Adapter {
    Context mContext;
    TextView mTvDes, mTvSprice;
    List<NewGoodsBean> mList;
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
        sortBy();
//        更新页面
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

            v.layout1.setTag(goods.getGoodsId());

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
            mList.clear();
        }
        mList.addAll(list);
//        刷新
        notifyDataSetChanged();

    }

    public void addDate(ArrayList<NewGoodsBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
     class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imPic)
        ImageView imPic;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tvSprice)
        TextView tvSprice;
        @BindView(R.id.layout1)
        LinearLayout layout1;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
//
        @OnClick(R.id.layout1)
        public void onGoodsItemClick(){
            int goodsId = (int) layout1.getTag();
            MFGT.gotoDetailsActivity(mContext,goodsId);

        }
    }
//
    private void sortBy(){
//        自动支持List排序，重写Comparaator方法，冒泡排序
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
//                he
                int result=0;
                switch (soryBy){
//                    时间排序
                    case I.SORT_BY_ADDTIME_ASC:
                        result=(int) (Long.valueOf(left.getAddTime())-Long.valueOf(right.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result=(int) (Long.valueOf(right.getAddTime())-Long.valueOf(left.getAddTime()));
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice()) - getPrice(right.getCurrencyPrice());

                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice()) - getPrice(left.getCurrencyPrice());

                        break;
                }
                return result;
            }
            private int getPrice(String price){
                price =price.substring(price.indexOf("￥")+1);
                return Integer.valueOf(price);
            }

        });
    }

}
