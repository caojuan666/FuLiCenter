package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
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
        if(resultCode ==RESULT_OK&&requestCode== I.REQUEST_CODE_NICK){
            CommonUtils.showLongToast(R.string.update_user_nick_success);
        }
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
