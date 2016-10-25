package cn.ucai.fulicenter.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqoeFragment;
import cn.ucai.fulicenter.fragment.CatrgoryFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.fragment.PersonCenterFragment;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    int index;
    int currentIndex;
    RadioButton[] ra ;
    @BindView(R.id.btnNewGoods)
    RadioButton btnNewGoods;
    @BindView(R.id.btnBoutique)
    RadioButton btnBoutique;
    @BindView(R.id.btnCategory)
    RadioButton btnCategory;
    @BindView(R.id.btnCollect)
    RadioButton btnCollect;
    @BindView(R.id.tvshop)
    RadioButton tvshop;
    Fragment[] mFragments;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqoeFragment mBoutiqoeFragment;
    CatrgoryFragment mCatrgoryFragment;
    PersonCenterFragment mPersonCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("main", "MainActivity.onCreate()");
        intiView();
        initFragment();
    }
//实例化
    private void initFragment() {
        mFragments = new Fragment[5];
        mNewGoodsFragment = new NewGoodsFragment();
        mBoutiqoeFragment = new BoutiqoeFragment();
        mCatrgoryFragment = new CatrgoryFragment();
        mPersonCenterFragment = new PersonCenterFragment();
        mFragments[0] = mNewGoodsFragment;
        mFragments[1]=mBoutiqoeFragment;
        mFragments[2] = mCatrgoryFragment;
        mFragments[3] = mPersonCenterFragment;
        getSupportFragmentManager()
                .beginTransaction()
               .add(R.id.fl, mNewGoodsFragment)
                .add(R.id.fl,mBoutiqoeFragment)
                .add(R.id.fl,mCatrgoryFragment)
                .hide(mBoutiqoeFragment)
                .hide(mCatrgoryFragment)
                .show(mNewGoodsFragment)
                .commit();
    }
    private void intiView() {
        ra = new RadioButton[5];
        ra[0]=btnNewGoods;
        ra[1]=btnBoutique;
        ra[2]=btnCategory;
        ra[3]=btnCollect;
        ra[4]= tvshop;
    }
    public void onCheckedChange(View view) {
        switch (view.getId()){
            case R.id.btnNewGoods:
                 index = 0;
            break;
            case R.id.btnBoutique:
                index = 1;
                break;
            case R.id.btnCategory:
                index = 2;
                break;
            case R.id.btnCollect:
                if(FuLiCenterApplication.getUser()==null){
                    MFGT.gotoLogin(this);
                }else{
                    index = 3;
                }
                break;
            case R.id.tvshop:
                index = 4;
                break;
        }
        setFragment();
    }
    private void setFragment() {
        L.e(TAG, "index:" + index);
        if(index!=currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragments[currentIndex]);
                if(!mFragments[index].isAdded()){
                    ft.add(R.id.fl,mFragments[index]);
                }
//            解放
//            ft.show(mFragments[index]).commit();
            ft.show(mFragments[index]).commitAllowingStateLoss();

        }
        setRadioButtonStatus();
        currentIndex = index;

    }
    private void setRadioButtonStatus() {
        for(int i=0;i<ra.length;i++){
            if(i==index){
                ra[i].setChecked(true);
            }else{
                ra[i].setChecked(false);
            }
        }
    }
    public void onBackPressed(){
        finish();
    }

//    解放1
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(FuLiCenterApplication.getUser()!=null)
//        {
//            index = 3;
//        }
//        setFragment();
//    }
//    关闭1
    @Override
    protected void onResume() {
        super.onResume();
        if(index==3&&FuLiCenterApplication.getUser()==null)
        {
            index = 0;
        }
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super .onActivityResult(requestCode,resultCode,data);
        if(requestCode== I.REQUEST_CODE_LOGIN&&FuLiCenterApplication.getUser()!=null){
            index = 3;
        }

    }
}
