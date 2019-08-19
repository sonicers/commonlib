package com.sonicers.commonlib.i;

import androidx.fragment.app.Fragment;

import com.sonicers.commonlib.widget.LoadingDialog;

public interface IFragmentHandler {

    void replaceFragment(int viewid, Fragment fragment, String tag);

    void addFragment(int viewid, Fragment fragment, String tag);

    void showFragment(String tag);

    void hideFragment(String tag);

    void removeFragment(String tag);

    void removeTopFragment();

    boolean isFragmentVisible(String tag);

    boolean isFragmentExist(String tag);

    LoadingDialog getLoadingDialog();
}
