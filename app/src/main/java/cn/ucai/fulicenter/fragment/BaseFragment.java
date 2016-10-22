package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
// */
//将通用的代码提取出来继承Frament,并让其他程序拥有initView();
//initDate();
//setListener();的方法继承BaseFragment
public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        initDate();
        setListener();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    protected  abstract void initView() ;
    protected  abstract void initDate();
    protected  abstract void setListener();
}
