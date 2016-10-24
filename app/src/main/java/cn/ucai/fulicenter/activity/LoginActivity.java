package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends BaseActivty {
    private static  final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.tv_user)
    EditText tv_user;
    @BindView(R.id.tv_userPassword)
    EditText tv_userPassword;
    @BindView(R.id.btn_loign)
    Button btnLoign;
    @BindView(R.id.btn_resign)
    Button btnResign;

    String username ;
    String password;
    LoginActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext=this;
        super.onCreate(savedInstanceState);
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

    @OnClick({R.id.btn_loign, R.id.btn_resign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loign:
//                登录时验证非空
                checkInput();
                break;
            case R.id.btn_resign:
                MFGT.gotoRegsiter(this);
                break;
        }
    }

    private void checkInput() {
          username = tv_user.getText().toString().trim();
        password = tv_userPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
            tv_user.requestFocus();
        }else  if(TextUtils.isEmpty(password)){
            tv_userPassword.requestFocus();
            return;
        }
        login();

    }

    private void login() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.logining));
        pd.show();
        L.e(TAG,"username="+username+",password="+password);
        NetDao.login(mContext, username, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String json) {
                pd.dismiss();
                L.e(TAG,"json="+json);
                Result result = ResultUtils.getResultFromJson(json, User.class);

//                TAG
                if(result==null){
                    CommonUtils.showLongToast(R.string.login_fail);
                }else{
                    if(result.isRetMsg()){
                       User user = (User) result.getRetData();
                        L.e(TAG,"user="+user);
                        UserDao dao = new UserDao(mContext);
                        boolean isSucccess = dao.saveUser(user);
                        if(isSucccess){
                            SharePrefrenceUtils.getInstance(mContext).saveUsser(user.getMuserName());

                            FuLiCenterApplication.setUser(user);
                            MFGT.finish(mContext);
                        }else{
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }


                    }else {
                        if(result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                        }else if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showLongToast(R.string.login_fail_error_password);
                        }else {
                            CommonUtils.showLongToast(R.string.login_fail);

                        }
                    }
                }

            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode== I.REQUEST_CODE_REGISTER){
            String name = data.getStringExtra(I.User.USER_NAME);
            tv_user.setText(name);

        }
    }
}
