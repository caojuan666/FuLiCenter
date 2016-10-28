package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;

public class OrderActivity extends BaseActivty {

    @BindView(R.id.et_cart_username)
    EditText etCartUsername;
    @BindView(R.id.et_cart_username_phone)
    EditText etCartUsernamePhone;
    @BindView(R.id.et_cart_username_street_address)
    EditText etCartUsernameStreetAddress;
    @BindView(R.id.spin_order_province)
    Spinner spinOrderProvince;
    @BindView(R.id.order_price)
    TextView orderPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        String cartIds = getIntent().getStringExtra(I.Cart.ID);

    }

    @Override
    protected void intiView() {


    }

    @OnClick(R.id.order_buy)
    public void onClick() {
    }
}
