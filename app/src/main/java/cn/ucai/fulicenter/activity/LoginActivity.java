package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

public class LoginActivity extends BaseActivty {

    @BindView(R.id.tv_user)
    EditText tv_user;
    @BindView(R.id.tv_userPassword)
    EditText tv_userPassword;
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
                MFGT.gotoRegsiter(this);
                break;
        }
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
