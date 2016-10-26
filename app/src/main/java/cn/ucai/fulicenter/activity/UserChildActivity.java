package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class UserChildActivity extends BaseActivty {

    @BindView(R.id.tv_child_user_nickname)
    TextView tvChildUserNickname;
    @BindView(R.id.imageView_child_user_avatar)
    ImageView imageViewChildUserAvatar;
    @BindView(R.id.tv_child_user_name)
    TextView tvChildUserName;

    UserChildActivity mContext;
    User user =null;
    OnSetAvatarListener mOnSetAvatarListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_child);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }
//判断
    @Override
    protected void initData() {
     user = FuLiCenterApplication.getUser();
        if(user==null){
            finish();
            return;
        }
        showInfo();
    }
    @Override
    protected void intiView() {
//        点击返回的标志
//        DisplayUtils.initBackWithTitle(mContext,getResources().getString(R.string.user_profile));

    }
    @OnClick({R.id.tv_child_user_nickname, R.id.imageView_child_user_avatar, R.id.tv_child_user_name, R.id.btton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_child_user_nickname:
                MFGT.gotoUpdateNick(mContext);
                break;
            case R.id.imageView_child_user_avatar:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext, R.id.layout_update_avatar,
                        user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.tv_child_user_name:
                CommonUtils.showLongToast(R.string.user_name_connot_be_modify);
                break;
            case R.id.btton:
//                进入个人中心之后退出按钮
                logout();
                break;
        }
    }
//进入个人中心之后点击退出按钮
    private void logout() {
        if(user!=null){
            SharePrefrenceUtils.getInstance(mContext).removeUser();
//
            FuLiCenterApplication.setUser(null);
//            跳转登录
            MFGT.gotoLogin(mContext);

        }
        finish();
    }
    @Override
    protected void onResume() {

        super.onResume();
        showInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode, data, imageViewChildUserAvatar);
        if(requestCode== I.REQUEST_CODE_NICK){
            CommonUtils.showLongToast(R.string.update_user_nick_success);
        }

        if(requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
//            上传头像
            updateAvatar();


        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext, user.getMavatarPath()+"/"+user.getMuserName()+user.getMavatarSuffix()));
        L.e("file="+file.exists());
        L.e("file="+file.getAbsolutePath());
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.update_user_avatar));
        pd.show();
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e("s="+s);
                Result result = ResultUtils.getResultFromJson(s, User.class);
                L.e("result="+result);
                if(result==null){
                CommonUtils.showLongToast(R.string.update_user_avatar_fail);


                }else if(result.isRetMsg()){
                    User u = (User) result.getRetData();
                    ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u), mContext, imageViewChildUserAvatar);
                    CommonUtils.showLongToast(R.string.update_user_avatar_success);

//                    上传失败
                }else {
                    CommonUtils.showLongToast(R.string.update_user_avatar_fail);

                }

            }

            @Override
            public void onError(String error) {
                L.e("error="+error);

            }
        });
    }

    private void showInfo(){
        user = FuLiCenterApplication.getUser();
//
        if(user!=null){
//            下载头像
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, imageViewChildUserAvatar);
            //            得到用户名和昵称
            tvChildUserName.setText(user.getMuserName());
            tvChildUserNickname.setText(user.getMuserNick());
        }
    }
}
