package com.neu.cloudfiles.di.component;

import android.app.Activity;
import android.content.Context;

import com.neu.cloudfiles.di.module.ActivityModule;
import com.neu.cloudfiles.di.scope.ContextLife;
import com.neu.cloudfiles.di.scope.PerActivity;
import com.neu.cloudfiles.ui.fileDetail.FileDetailActivity;
import com.neu.cloudfiles.ui.login.LoginActivity;
import com.neu.cloudfiles.ui.register.RegisterActivity;
import com.neu.cloudfiles.ui.shareList.ShareListActivity;
import com.neu.cloudfiles.ui.upload.FileUploadActivity;
import com.neu.cloudfiles.ui.userInfo.UserInfoActivity;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(LoginActivity loginActivity);

    void inject(FileDetailActivity activity);

    void inject(FileUploadActivity activity);

    void inject(ShareListActivity activity);

    void inject(UserInfoActivity activity);

    void inject(RegisterActivity activity);
}
