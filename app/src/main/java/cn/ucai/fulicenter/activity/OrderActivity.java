package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class OrderActivity extends BaseActivty implements PaymentHandler {
    private static final String TAG = OrderActivity.class.getSimpleName();
    private static String URL = "http://218.244.151.190/demo/charge";
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

    User user = null;
    String cartIds = "";
    OrderActivity mContext;
    ArrayList<CartBean> mList = null;
    String[] ids = new String[]{};
    int ranPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void initData() {
        cartIds = getIntent().getStringExtra(I.Cart.ID);
        user = FuLiCenterApplication.getUser();
        if (cartIds == null || cartIds.equals("") || user == null) {
            finish();
        }

        ids = cartIds.split(",");
        geOrderList();
    }

    private void geOrderList() {
        NetDao.downloadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    mList.addAll(list);
                    sumPrice();
                }

            }

            @Override
            public void onError(String error) {

            }
        });

    }

    @Override
    protected void intiView() {
//        DisplayUtils.initBackWithTitle(mContext, getString(R.string.confirm_order));

    }

    @OnClick(R.id.order_buy)
    public void checkOrder() {
        String receiverName = etCartUsername.getText().toString();
        if (TextUtils.isEmpty(receiverName)) {
            etCartUsername.setError("收貨人姓名不能為空");
            etCartUsername.requestFocus();
            return;
        }
        String moble = etCartUsernamePhone.getText().toString();
        if (TextUtils.isEmpty(moble)) {
            etCartUsernamePhone.setError("手機號碼不能為空");
            etCartUsernamePhone.requestFocus();
            return;
        }
        if (!moble.matches("[\\d]{11}")) {
            etCartUsernamePhone.setError("手機號碼格式有無");
            etCartUsernamePhone.requestFocus();
            return;
        }
        String address = etCartUsernameStreetAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            etCartUsernameStreetAddress.setError("街道地址不能为空");
            etCartUsernameStreetAddress.requestFocus();
            return;
        }
        String area = spinOrderProvince.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(OrderActivity.this, "收穫地址不為空", Toast.LENGTH_SHORT).show();
            return;
        }
        gotoStatements();

    }


    //计算价格
    private void sumPrice() {
        ranPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                for (String id : ids) {
                    L.e(TAG, "oreder.id=" + id);
                    if (id.equals(String.valueOf(c.getId()))) {
//                        cartIds += c.getId() + ",";
                        ranPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    }
                }
            }

        }
        orderPrice.setText("合计：￥" + Double.valueOf(ranPrice));
    }


    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    private void gotoStatements() {
        L.e(TAG, "ranPrice=" + ranPrice);
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());


        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", ranPrice*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
            if (data != null) {
                /**
                 * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
                 * error_msg：支付结果信息
                 */
                int code = data.getExtras().getInt("code");
                L.e(TAG, "code:" + code);
                String errorMsg = data.getExtras().getString("error_msg");
        }
    }


}
