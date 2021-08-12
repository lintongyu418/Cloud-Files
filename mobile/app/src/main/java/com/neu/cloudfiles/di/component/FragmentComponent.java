package com.neu.cloudfiles.di.component;

import android.app.Activity;
import android.content.Context;

import com.neu.cloudfiles.di.module.FragmentModule;
import com.neu.cloudfiles.di.scope.ContextLife;
import com.neu.cloudfiles.di.scope.PerFragment;
import com.neu.cloudfiles.ui.download.DownloadListFragment;
import com.neu.cloudfiles.ui.files.FilesFragment;
import com.neu.cloudfiles.ui.shares.SharesFragment;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(FilesFragment fragment);

    void inject(DownloadListFragment downloadListFragment);

    void inject(SharesFragment fragment);

}
