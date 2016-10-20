package cn.ucai.fulicenter.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CategoryGoupAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.net.ConvertUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;

/**
 * Created by Administrator on 2016/10/20 0020.
 */

public class CatrgoryFragment extends BaseFragment {
    int groupCount;
    CategoryGoupAdapter mAdapter;
    MainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this,layout);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }
//    设置箭头
    @Override
    protected void initView() {
        mChildList = new ArrayList<>();
        mGroupList = new ArrayList<>();
        mContext = (MainActivity) getContext();
        mAdapter = new CategoryGoupAdapter(mContext,mChildList,mGroupList);
        elvCategory.setGroupIndicator(null);
        elvCategory.setAdapter(mAdapter);


    }
//调动NetDao中的放啊发

    /**下载数据
     *
     */
    @Override
    protected void initDate() {
        downloadCatrgoryGroup();

    }
    private void downloadCatrgoryGroup() {
        NetDao.downloadCatrgoryGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null&&result.length>0) {
                    ArrayList<CategoryGroupBean> groupList = ConvertUtils.array2List(result);
                    L.e("groupList=" + groupList);
                    mGroupList.addAll(groupList);
                    for (int i=0; i<groupList.size();i++) {
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        CategoryGroupBean g= groupList.get(i);
                        downloadChild(g.getId(),i);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e("error="+error);

            }
        });
    }
            private void downloadChild(int id, final int index) {
                NetDao.downloadCatrgoryChild(mContext,id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] result) {

                        groupCount ++;
                        L.e("downloadChild,result="+result);
                        if(result!=null&&result.length>0){
                            ArrayList<CategoryChildBean> childList= ConvertUtils.array2List(result);
                            L.e("childList="+childList.size());
                            mChildList.add(index,childList);
                        }
                        if(groupCount==mGroupList.size()){
                            mAdapter.initData(mGroupList,mChildList);
                        }
                    }
                    @Override
                    public void onError(String error) {
                        L.e("error="+error);

                    }
                });
            }




    @Override
    protected void setListener() {

    }
}
