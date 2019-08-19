package com.sonicers.commonlib.component;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sonicers.commonlib.i.IFragmentHandler;
import com.sonicers.commonlib.widget.LoadingDialog;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.List;

public class BaseActivity extends RxAppCompatActivity implements IFragmentHandler {

    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FullScreenUtil.initFullScreen(this);
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.Companion.get(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (isInvalidContext()) {
            return;
        }
        if (loadingDialog != null) {
            loadingDialog.hide();
        }
    }

    private boolean isInvalidContext() {
        return (isDestroyed() || isFinishing());
    }

    /**
     * Fragment替换方式，销毁前一个
     */
    public void replaceFragment(int viewId, Fragment fragment, String tag) {
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);//加入返回
            transaction.replace(viewId, fragment, tag);
            transaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个Fragment
     */
    public void addFragment(int viewId, Fragment fragment, String tag) {
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(viewId, fragment, tag).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 显示一个已存在的Fragment
     */
    public void showFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }
        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏一个Fragment
     */
    public void hideFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }
        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 移除一个Fragment
     */
    public void removeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }
        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 移除最后添加的Fragment
     */
    public void removeTopFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() == 0) {
            return;
        }
        getSupportFragmentManager().beginTransaction().remove(fragments.get(fragments.size() - 1)).commitAllowingStateLoss();
    }

    /**
     * 判断Fragment是否显示
     */
    public boolean isFragmentVisible(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            return false;
        }
        return fragment.isVisible();
    }

    /**
     * 判断Fragment是否存在
     */
    public boolean isFragmentExist(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            return false;
        }
        return true;
    }

    @Override
    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }
}