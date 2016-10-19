package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

public class Main2Activity extends BaseActivty {
    private static long sleepTime=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main2);
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
//                startActivity(new Intent(Main2Activity.this,MainActivity.class));
                MFGT.gotoMainActivity(Main2Activity.this);





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
