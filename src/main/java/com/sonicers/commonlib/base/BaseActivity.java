package com.sonicers.commonlib.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sonicers.commonlib.widget.CustomProgressDialog;


/**
 * 整个app的Activity基类
 */
public abstract class BaseActivity<P extends BasePresenter> extends Activity {
    protected P mPresenter;

    protected abstract P createPresenter();

    protected CustomProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }


    public void showLoadingDialog() {
        showLoadingDialog("", true);
    }


    public void showLoadingDialog(String text) {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(text);
        } else {
            showLoadingDialog(text, true);
        }
    }

    public void showLoadingDialog(String text, boolean cancelEnable) {
        if (isFinishing()) {
            return;
        }
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setCancelable(cancelEnable);
        mProgressDialog.setCanceledOnTouchOutside(cancelEnable);
        mProgressDialog.setMessage(text);
        mProgressDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


}
