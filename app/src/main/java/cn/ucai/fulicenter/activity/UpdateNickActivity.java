package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class UpdateNickActivity extends BaseActivty {
    private static final String TAG = UpdateNickActivity.class.getSimpleName();

    UpdateNickActivity mContext;
    @BindView(R.id.et_update_user_name)
    EditText etUpdateUserName;
    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }
//将数据
    @Override
    protected void initData() {
       user = FuLiCenterApplication.getUser();
        if (user != null) {
            etUpdateUserName.setText(user.getMuserNick());
            etUpdateUserName.setSelectAllOnFocus(true);
        }else{
            finish();
        }

    }

    @Override
    protected void intiView() {
//        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.update_user_nick));

    }

    @OnClick(R.id.button)
    public void checkNick() {
        if(user!=null){
            String nick = etUpdateUserName.getText().toString().trim();
            if(nick.equals(user.getMuserNick())){
                CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
            }else if(TextUtils.isEmpty(nick)){
                CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
            }else {
            updateNick(nick);
            }
        }
    }
//
    private void updateNick(String nick) {
//        更新中
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.update_user_nick));
        pd.show();
        NetDao.updateNick(mContext, user.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                pd.dismiss();
                L.e(TAG,"s="+s);
                Result result = ResultUtils.getResultFromJson(s, User.class);
//                TAG
                if(result==null){
                    CommonUtils.showLongToast(R.string.login_fail);
                }else{
                    if(result.isRetMsg()){
                        User u = (User) result.getRetData();
                        L.e(TAG,"user="+u);
                        UserDao dao = new UserDao(mContext);
                        boolean isSucccess = dao.updateUser(u);
                        if(isSucccess){
                            SharePrefrenceUtils.getInstance(mContext).saveUsser(user.getMuserName());

                            FuLiCenterApplication.setUser(u);
                            MFGT.finish(mContext);
                        }else{
//                            失败
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                    }else {
                        if(result.getRetCode()==I.MSG_USER_SAME_NICK){
                            CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
                        }else if(result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL){
                            CommonUtils.showLongToast(R.string.update_fail);
                        }else {
                            CommonUtils.showLongToast(R.string.update_fail);

                        }
                    }
                }
                pd.dismiss();

            }
            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
                L.e(TAG,"error="+error);

            }
        });
    }
}