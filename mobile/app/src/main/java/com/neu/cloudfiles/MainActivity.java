package com.neu.cloudfiles;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.neu.cloudfiles.base.BaseActivity;
import com.neu.cloudfiles.bean.ShareFileListDo;
import com.neu.cloudfiles.ui.download.DownloadListFragment;
import com.neu.cloudfiles.ui.files.FilesFragment;
import com.neu.cloudfiles.ui.shares.SharesFragment;


import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;

@Route(path = "/cloudFile/MainActivity")
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    private ISupportFragment[] mFragments = new ISupportFragment[4];
    private long mExitTime;
    private int preIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initView() {
        mNavigation.setOnNavigationItemSelectedListener(this);
        ISupportFragment homeFragment = findFragment(SharesFragment.class);
        if (homeFragment == null) {
            mFragments[0] = SharesFragment.newInstance();
            mFragments[1] = FilesFragment.newInstance();
            mFragments[2] = DownloadListFragment.newInstance();
            loadMultipleRootFragment(R.id.layout_fragment, 0,
                    mFragments[0],
                    mFragments[1],
                    mFragments[2]);
        } else {
            // 这里我们需要拿到mFragments的引用
            mFragments[0] = homeFragment;
            mFragments[1] = findFragment(FilesFragment.class);
            mFragments[2] = findFragment(DownloadListFragment.class);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_shares:
                mToolbar.setTitle(R.string.title_shares);
                showHideFragment(mFragments[0], mFragments[preIndex]);
                preIndex = 0;
                break;
            case R.id.navigation_files:
                mToolbar.setTitle(R.string.title_files);
                showHideFragment(mFragments[1], mFragments[preIndex]);
                preIndex = 1;
                break;
            case R.id.navigation_download:
                mToolbar.setTitle(R.string.title_download);
                showHideFragment(mFragments[2], mFragments[preIndex]);
                preIndex = 2;
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShare) {
            ARouter.getInstance().build("/cloudFile/FileUploadActivity")
                    .withString("targetFolderPath", ((FilesFragment) mFragments[1]).getUserCurrentFolderPath())
                    .navigation();
        } else if (item.getItemId() == R.id.menuUser) {
        } else if (item.getItemId() == R.id.menuScan) {
            this.startScanQR();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.showShort(R.string.exit_system);
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startScanQR() {
        /*以下是启动我们自定义的扫描活动*/
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setBeepEnabled(true);
        /*设置启动我们自定义的扫描活动，若不设置，将启动默认活动*/
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String url = result.getContents();
        Gson gson = new Gson();
        ShareFileListDo shareFileListDo = gson.fromJson(url, ShareFileListDo.class);
        if (null != shareFileListDo) {
            ARouter.getInstance().build("/cloudFile/SharedFileList")
                    .withObject("shareSecret", shareFileListDo)
                    .navigation();
        }
    }
}
