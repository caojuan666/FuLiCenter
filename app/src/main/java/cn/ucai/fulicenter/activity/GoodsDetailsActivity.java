package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Albums;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailsActivity extends BaseActivty {
    private static final String TAG = GoodsDetailsActivity.class.getSimpleName();

    int goodsID;

    GoodsDetailsActivity mContext;
    @BindView(R.id.title_back_imageView)
    ImageView titleBackImageView;
    @BindView(R.id.goodsDetail_goodsEngTitle_textView)
    TextView goodsDetailGoodsEngTitleTextView;
    @BindView(R.id.goodsDetail_goodsChiTitle_textView)
    TextView goodsDetailGoodsChiTitleTextView;
    @BindView(R.id.goodsDetail_shopPrice_textView)
    TextView goodsDetailShopPriceTextView;
    @BindView(R.id.goodsDetail_cuurentPrice_textView)
    TextView goodsDetailCuurentPriceTextView;
    @BindView(R.id.goodsDetail_showGoods_SlideAutoLoopView)
    SlideAutoLoopView goodsDetailShowGoodsSlideAutoLoopView;
    @BindView(R.id.goodsDetail_FlowIndicator)
    FlowIndicator goodsDetailFlowIndicator;
    @BindView(R.id.goodsDetail_description_webView)
    WebView goodsDetailDescriptionWebView;
    GoodsDetailsBean goodsDetailsBean;
    boolean isCollects = false;
    @BindView(R.id.goodsDetail_collect_imageView)
    ImageView goodsDetailCollectImageView;
    @BindView(R.id.goodsDetail_share_imageView)
    ImageView goodsDetailShareImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodsID = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details,goodsid:" + goodsID);
        if (goodsID == 0) {
            finish();
        }
        mContext = this;
        super.onCreate(savedInstanceState);
//        initView();
//        initData();
//        setListener();

    }
    @OnClick()

    @Override
    protected void setListener() {

    }


    @Override
    protected void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsID, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details=" + result);
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }

            private void showGoodDetails(GoodsDetailsBean details) {
                goodsDetailGoodsEngTitleTextView.setText(details.getGoodsEnglishName());
                goodsDetailGoodsChiTitleTextView.setText(details.getGoodsName());
                goodsDetailShopPriceTextView.setText(details.getShopPrice());
                goodsDetailCuurentPriceTextView.setText(details.getCurrencyPrice());
                goodsDetailShowGoodsSlideAutoLoopView.startPlayLoop(goodsDetailFlowIndicator, getAlbumImgUrl(details), getAlbumImgCount(details));
                goodsDetailDescriptionWebView.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);

            }

            private int getAlbumImgCount(GoodsDetailsBean goodsDetailsBean) {
                if (goodsDetailsBean.getProperties() != null && goodsDetailsBean.getProperties().length > 0) {
                    return goodsDetailsBean.getProperties()[0].getAlbums().length;
                }
                return 0;

            }

            private String[] getAlbumImgUrl(GoodsDetailsBean goodsDetailsBean) {
                String[] urls = new String[]{};
                if (goodsDetailsBean.getProperties() != null && goodsDetailsBean.getProperties().length > 0) {
                    Albums[] albums = goodsDetailsBean.getProperties()[0].getAlbums();
                    urls = new String[albums.length];
                    for (int i = 0; i < albums.length; i++) {
                        urls[i] = albums[i].getImgUrl();
                    }
                }
                return urls;
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details,error=" + error);
                CommonUtils.showShortToast(error);

            }
        });


    }

    @Override
    protected void intiView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollects();
    }

    @OnClick(R.id.title_back_imageView)
    public void onBackClick() {
        MFGT.finish(this);
    }

    @OnClick(R.id.goodsDetail_collect_imageView)
    public void onClick() {
        User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLogin(mContext);
        } else {
            if (isCollects) {
                NetDao.deleteCollects(mContext, user.getMuserName(), goodsID, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollects = !isCollects;
                            updateGoodsCollects();
                            L.e("deleteCollects=");
                        }
                    }

                    @Override
                    public void onError(String error) {


                    }
                });
            } else {
                NetDao.addCollects(mContext, user.getMuserName(), goodsID, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollects = !isCollects;
                            updateGoodsCollects();
                            L.e("updateGoodsCollects=");

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }

    public void onBackPressed() {
        MFGT.finish(this);
    }

    private void isCollects() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isCollects(mContext, user.getMuserName(), goodsID, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollects = true;
                        updateGoodsCollects();

                    } else {
                        isCollects = false;
                    }
                }

                @Override
                public void onError(String error) {


                }
            });
        }
        updateGoodsCollects();
    }

    private void updateGoodsCollects() {
        if (isCollects) {
            goodsDetailCollectImageView.setImageResource(R.mipmap.bg_collect_out);
        } else {
            goodsDetailCollectImageView.setImageResource(R.mipmap.bg_collect_in);
        }
    }
    @OnClick(R.id.goodsDetail_cart_imageView)
    public void addCart(){
        User user = FuLiCenterApplication.getUser();
        if(user!=null){
            NetDao.addCart(mContext, user.getMuserName(), goodsID, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null&&result.isSuccess()){
                        CommonUtils.showLongToast(R.string.add_goods_success);
                    }else {
                        CommonUtils.showLongToast(R.string.add_goods_fail);
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showLongToast(R.string.add_goods_fail);
                    L.e(TAG,"error="+error);

                }
            });
        }else {
            MFGT.gotoLogin(mContext);

        }

    }

    @OnClick(R.id.goodsDetail_share_imageView)
    public void shareCollects() {
        showShare();
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

}
