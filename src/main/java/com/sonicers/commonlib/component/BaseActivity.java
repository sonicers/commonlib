package com.sonicers.commonlib.component;

import android.os.Bundle;

import com.sonicers.commonlib.widget.LoadingDialog;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

public class BaseActivity extends RxAppCompatActivity {

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
}