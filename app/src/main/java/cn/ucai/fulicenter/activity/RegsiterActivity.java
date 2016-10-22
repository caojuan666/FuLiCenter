package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.DisplayUtils;

public class RegsiterActivity extends BaseActivty {
    private  static  final String TAG = RegsiterActivity.class.getSimpleName();

    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_NickName)
    TextView tvNickName;
    @BindView(R.id.tv_userPassword)
    TextView tvUserPassword;
    @BindView(R.id.okPassword)
    TextView okPassword;

    String username ;
    String nickName ;
    String password ;
    String rpassword ;

    RegsiterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regsiter);
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
        DisplayUtils.initBackWithTitle(this,"账户注册");

    }
//
    @OnClick(R.id.btn_resign)
    public void onClick() {
//        trim：：去掉头尾的空格
        username = tvUser.getText().toString().trim();
         nickName = tvNickName.getText().toString().trim();
         password = tvUserPassword.getText().toString().trim();
         rpassword = okPassword.getText().toString().trim();
//      （1）  判断yonghu是否为空
        if(TextUtils.isEmpty(username)){
//            若为空
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
//            告诉不能为空
            tvUser.requestFocus();
            return;
//            验证用户名
        }else if(!username.matches("[a-zA-Z]\\w{5,15}")){
            CommonUtils.showShortToast(R.string.illegal_user_name);
            tvUser.requestFocus();
            return;
        }else if(TextUtils.isEmpty(nickName)){
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            tvNickName.requestFocus();
            return;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            tvUserPassword.requestFocus();
            return;
        }else if(TextUtils.isEmpty(rpassword)){
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
        }else if(!password.equals(rpassword)){
            CommonUtils.showShortToast(R.string.two_input_password);
            return;
        }
//        验证之后取调用注册方法
    register();

    }
//实现注册需要信息
    private void register() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
//       先 有输入 就得进行网络请求在NetDao中
        NetDao.register(mContext, username, nickName, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
//                判断返回结果
                if(result==null){
                    CommonUtils.showShortToast(R.string.register_fail);
//注册chengong
                }else {
                    if(result.isRetMsg()){
                        CommonUtils.showLongToast(R.string.register_success);
                        MFGT.finish(mContext);
//                        若注册失败了
                    }else {
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                        tvUser.requestFocus();
//

                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(R.string.register_fail);
                    L.e(TAG,"register error="+error);
            }
        });

    }
}
