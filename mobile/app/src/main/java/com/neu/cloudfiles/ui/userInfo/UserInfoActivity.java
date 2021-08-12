package com.neu.cloudfiles.ui.userInfo;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.neu.cloudfiles.MainActivity;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseActivity;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.event.UserLogoutEvent;
import com.neu.cloudfiles.utils.RxBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/24.
 */
@Route(path = "/cloudFile/UserInfo")
public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.View {
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.user_img)
    ImageView mUserImage;
    @BindView(R.id.user_tel)
    TextView mUserTelephone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        String token = SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.TOKEN_KEY, "");
        this.mPresenter.getUserInfo(token);
        Object a = RxBus.getInstance().toFlow(UserLogoutEvent.class)
                .subscribe(new Consumer<UserLogoutEvent>() {
                    @Override
                    public void accept(UserLogoutEvent a) throws Exception {
                        // add a new download task
                        UserInfoActivity.this.finish();
                    }
                });
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        this.logoutSuccess();
    }

    @Override
    public void getUserInfoSuccess(UserVo user) {
        mUsername.setText(user.getUsername());
        mUserTelephone.setText(user.getTelephone());
        if (user.getImageUrl() == null) {
            user.setImageUrl("https://img.icons8.com/bubbles/2x/user-male.png");
        }
        Glide.with(getApplicationContext()).load(user.getImageUrl()).into(mUserImage);
    }

    @Override
    public void logoutSuccess() {
        SPUtils.getInstance(Constant.SHARED_NAME).clear();
        ARouter.getInstance().build("/cloudFile/Login").navigation();
        RxBus.getInstance().post(new UserLogoutEvent());
    }
}
