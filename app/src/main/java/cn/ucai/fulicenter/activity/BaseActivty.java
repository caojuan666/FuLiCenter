package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

/**
 *
 * 将activity中通用的方法放入BaseActivity中，并切BaseActivity继承AppCompatActivity
 */
public abstract class BaseActivty extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intiView();
        initData();
        setListener();
    }
    protected abstract void setListener();//
    protected abstract void initData();
    protected abstract void intiView();
    public void onBackPressed(){//跳转页面
        MFGT.finish(this);
    }
}
