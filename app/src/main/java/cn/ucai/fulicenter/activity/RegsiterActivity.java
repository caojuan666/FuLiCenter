package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.DisplayUtils;

public class RegsiterActivity extends BaseActivty {

    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_NickName)
    TextView tvNickName;
    @BindView(R.id.tv_userPassword)
    TextView tvUserPassword;
    @BindView(R.id.okPassword)
    TextView okPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_regsiter);
        ButterKnife.bind(this);
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

    @OnClick(R.id.btn_resign)
    public void onClick() {
    }
}
