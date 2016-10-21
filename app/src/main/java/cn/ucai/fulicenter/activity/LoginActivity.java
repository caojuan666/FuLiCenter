package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;

public class LoginActivity extends BaseActivty {

    @BindView(R.id.tv_userPassword)
    EditText tvUserPassword;
    @BindView(R.id.tv_NickName)
    EditText tvNickName;
    @BindView(R.id.btn_loign)
    Button btnLoign;
    @BindView(R.id.btn_resign)
    Button btnResign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
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

    }

    @OnClick({R.id.btn_loign, R.id.btn_resign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loign:
                break;
            case R.id.btn_resign:

                break;
        }
    }
}
