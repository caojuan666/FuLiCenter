package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public class BoutiqueBeanAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;
    private int foooterString;
    boolean isMore;
    public BoutiqueBeanAdapter(Context context, ArrayList<BoutiqueBean> list) {
       mContext = context;
       mList = new ArrayList<>();
        mList.addAll(list);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new BoutiqueViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_boutique, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).tvfooter.setText(getFoooterString());

        }if(holder instanceof BoutiqueViewHolder){
            BoutiqueBean boutiqueBean = mList.get(position);
            ImageLoader.downloadImg(mContext,((BoutiqueViewHolder) holder).ivBoutiqueImg,boutiqueBean.getImageurl());
            ((BoutiqueViewHolder) holder).tvBoutiTitle.setText(boutiqueBean.getTitle());
            ((BoutiqueViewHolder) holder).tvName.setText(boutiqueBean.getName());
            ((BoutiqueViewHolder) holder).tvInformation.setText(boutiqueBean.getDescription());
        }


    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.hashCode() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public int getFoooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    public void setMore(boolean b) {
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if(mList!=null){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();

    }

    public void addDate(ArrayList<BoutiqueBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiTitle)
        TextView tvBoutiTitle;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvInformation)
        TextView tvInformation;
        @BindView(R.id.layout_boutique_item)
        RelativeLayout layoutBoutiqueItem;

         BoutiqueViewHolder(View view) {
             super(view);
             ButterKnife.bind(this, view);
        }
    }
}
