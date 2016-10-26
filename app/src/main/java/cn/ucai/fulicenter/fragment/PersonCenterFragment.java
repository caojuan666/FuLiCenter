package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonCenterFragment extends BaseFragment {
    private static final String TAG = PersonCenterFragment.class.getSimpleName();
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    MainActivity mContext;
    User user = null;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_person_center, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getActivity();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }


    @Override
    protected void initView() {


    }


    @Override
    protected void initDate() {
        user = FuLiCenterApplication.getUser();
        L.e(TAG, "user=" + user);
//        若用户为空
        if (user == null) {
            MFGT.gotoLogin(mContext);

        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivUserAvatar);
            tvUserName.setText(user.getMuserNick());
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        L.e(TAG, "user=" + user);
//        若用户为空
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivUserAvatar);
            tvUserName.setText(user.getMuserNick());
            getCollectCount();
            syncUserInfo();
        }

    }

    //d点击进入个人中心
    @OnClick({R.id.tv_center_settings, R.id.center_user_info})
    public void gotoSetting() {
        MFGT.gotoSetting(mContext);
    }

    private void syncUserInfo() {
        NetDao.syncUserInfo(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result != null) {
                    User u = (User) result.getRetData();
                    if (!user.equals(u)) {
                        UserDao dao = new UserDao(mContext);
//                        保存数据
                        boolean b = dao.saveUser(u);
                        if (b) {
                            FuLiCenterApplication.setUser(u);
                            user = u;
                            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivUserAvatar);
                            tvUserName.setText(user.getMuserName());
                        }
                    }
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getCollectCount() {
        NetDao.getCollectCount(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    tvCollectCount.setText(result.getMsg());
                }else {
                    tvCollectCount.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {
                tvCollectCount.setText(String.valueOf(0));
                L.e(TAG,"error="+error);
            }
        });
    }
}

