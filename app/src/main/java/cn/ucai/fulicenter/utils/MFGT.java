package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueActivity;
import cn.ucai.fulicenter.activity.CategoryChildActivity;
import cn.ucai.fulicenter.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.activity.LoginActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.RegsiterActivity;
import cn.ucai.fulicenter.activity.UpdateNickActivity;
import cn.ucai.fulicenter.activity.UserChildActivity;
import cn.ucai.fulicenter.bean.CategoryChildBean;


public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoMainActivity(Activity context) {
        startActivity(context, MainActivity.class);
    }

    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        startActivity(context, intent);
    }

    public static void gotoDetailsActivity(Context context, int googId) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID, googId);
        startActivity(context, intent);
    }

    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void gotoBoutiqueActivity(Context context, int catId) {
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueActivity.class);
        intent.putExtra(I.Boutique.CAT_ID, catId);
        startActivity(context, intent);
    }

    public static void gotoCategoryChildActivity(Context context, int catId, String groupName, ArrayList<CategoryChildBean> list) {
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID, catId);
        intent.putExtra(I.CategoryGroup.NAME, groupName);
        intent.putExtra(I.CategoryChild.ID, list);
        startActivity(context, intent);
    }

    //    解放1
//    public static void gotoLogin(Activity context) {
//        startActivity(context, LoginActivity.class);
//    }
//    关闭2
    public static void gotoLogin(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        startActivityForResult(context, intent, I.REQUEST_CODE_LOGIN);
    }

    //    注册请求用户名
    public static void gotoRegsiter(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, RegsiterActivity.class);
        startActivityForResult(context, intent, I.REQUEST_CODE_REGISTER);


    }

    public static void startActivityForResult(Activity context, Intent intent, int requestCode) {

        context.startActivityForResult(intent, requestCode);
//        startActivity(context,RegsiterActivity.class);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


    }
//    跳转
    public static  void  gotoSetting(Activity context){
        startActivity(context, UserChildActivity.class);
    }

    public  static  void gotoUpdateNick(Activity context){
        startActivityForResult(context,new Intent(context, UpdateNickActivity.class),I.REQUEST_CODE_NICK);
    }
}