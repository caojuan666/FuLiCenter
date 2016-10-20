package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/20 0020.
 */

public class CategoryGoupAdapter extends BaseExpandableListAdapter {
//    拿到数量要写一个集合，然后进行构造方法
    Context mContext;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;//小类的数量
    ArrayList<CategoryGroupBean> mGroupList;//大类的数量
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivPictureP)
    ImageView ivPictureP;

    public CategoryGoupAdapter(Context context, ArrayList<ArrayList<CategoryChildBean>> childList, ArrayList<CategoryGroupBean> groupList) {
        mContext = context;
        mGroupList = new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList = new ArrayList<>();

        mChildList.addAll(childList);


    }

//获取大类总的数量（长度）
    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }
//获取晓得总的数量

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ? mChildList.get(groupPosition).size() : 0;
    }
//获取数据
    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList != null ? mGroupList.get(groupPosition) : null;
    }
//
    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ?
                mChildList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        GroupViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.category_group, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            view.getTag();
            holder = (GroupViewHolder) view.getTag();
        }
        CategoryGroupBean group = getGroup(groupPosition);
        if (group != null) {
            ImageLoader.downloadImg(mContext, holder.imageView, group.getImageUrl());
            holder.tvName.setText(group.getName());
            holder.ivPictureP.setImageResource(isExpanded ? R.mipmap.arrow2_down : R.mipmap.arrow2_up);

        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ChildViewHolder holder;

        if (view == null) {
            view = View.inflate(mContext, R.layout.category_child, null);
            holder = new ChildViewHolder(view);
            view.setTag(holder);


        }else{
            view.getTag();
            holder = (ChildViewHolder) view.getTag();
        }
        CategoryChildBean child = getChild(groupPosition, childPosition);
        if(child!=null){
            ImageLoader.downloadImg(mContext,holder.imageChildPicture,child.getImageUrl());
            holder.tvChildName.setText(child.getName());

        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupList,
                         ArrayList<ArrayList<CategoryChildBean>> childList) {
        if(mGroupList!=null){
            mGroupList.clear();
        }
        mGroupList.addAll(groupList);
        if(mGroupList!=null){
            mChildList.clear();
        }
        mChildList.addAll(childList);
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivPictureP)
        ImageView ivPictureP;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.imageChildPicture)
        ImageView imageChildPicture;
        @BindView(R.id.tvChildName)
        TextView tvChildName;
        @BindView(R.id.llM)
        LinearLayout llM;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
