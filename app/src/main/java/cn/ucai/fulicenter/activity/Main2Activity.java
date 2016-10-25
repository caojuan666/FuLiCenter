package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class Main2Activity extends BaseActivty {
    private static  final String TAG = Main2Activity.class.getSimpleName();

    private static long sleepTime=2000;
    Main2Activity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main2);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {

        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start  = System.currentTimeMillis();
                long costTime = System.currentTimeMillis()-start;
                if(sleepTime-costTime>0){
//耗时操作
                    try {
                        Thread.sleep(sleepTime-costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                User user = FuLiCenterApplication.getUser();
                L.e(TAG,"fulicenter.user="+user);
                String username = SharePrefrenceUtils.getInstance(mContext).getUser();
              L.e(TAG,"fulicenter.username="+username);
                if(user==null&&username!=null){
                    UserDao dao = new UserDao(mContext);
                    user = dao.getUser(username);
                    L.e(TAG,"database.user="+user);
                    if(user!=null){
                        FuLiCenterApplication.setUser(user);
                    }
                }

//                startActivity(new Intent(Main2Activity.this,MainActivity.class));
                MFGT.gotoMainActivity(Main2Activity.this);
                finish();




            }
        }).start();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void intiView() {

    }
}
