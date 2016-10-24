package cn.ucai.fulicenter;

import android.app.Application;

import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/17 0017.
 */


public class FuLiCenterApplication  extends Application{
    private static FuLiCenterApplication instance;
    private static String  username;
    private static User user;

    public FuLiCenterApplication(){
        instance = this;
    }

    public static User getUser() {
        return user;
    }

    public static FuLiCenterApplication getInstance(){
        if(instance==null){
            instance= new FuLiCenterApplication();
        }
        return instance;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }
}
