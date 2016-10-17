package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class NewGoodAdapter  extends Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mList;

    public NewGoodAdapter(Context mContext, ArrayList<NewGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
//判断
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if(viewType==I.TYPE_FOOTER){
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer,null));
        }else{
            holder = new GoodsViewHolder(View.inflate(mContext,R.layout.item_newgoods,null));
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
//返回
    @Override
    public int getItemCount() {
        return mList!=null?mList.size()+1:1;
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

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View inflate) {
            super(inflate);
        }
    }

    private class GoodsViewHolder extends RecyclerView.ViewHolder {
        public GoodsViewHolder(View inflate) {
            super(inflate);
        }
    }
}
