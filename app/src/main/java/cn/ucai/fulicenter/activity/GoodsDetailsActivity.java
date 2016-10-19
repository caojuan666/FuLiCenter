package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Albums;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.CommonUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailsActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodsID = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details,goodsid:" + goodsID);
        if (goodsID == 0) {
            finish();
        }
        mContext = this;
        initView();
        initData();
        setListener();

    }

    private void setListener() {

    }

    private void initData() {
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
                goodsDetailShowGoodsSlideAutoLoopView.startPlayLoop(goodsDetailFlowIndicator,getAlbumImgUrl(goodsDetailsBean),getAlbumImgCount(goodsDetailsBean));
                goodsDetailDescriptionWebView.loadDataWithBaseURL(null,details.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);

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
                    for (int i = 0;i<albums.length;i++){
                        urls[i]=albums[i].getImgUrl();
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

    private void initView() {

    }
    @OnClick(R.id.title_back_imageView)
    public void onBackClick(){
        MFGT.finish(this);
    }
    public void onBackPressed(){
        MFGT.finish(this);
    }
}
