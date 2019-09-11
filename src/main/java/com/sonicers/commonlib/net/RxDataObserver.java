package com.sonicers.commonlib.net;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.sonicers.commonlib.widget.LoadingDialog;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * 数据观察者,并集成dialog
 */
public abstract class RxDataObserver<M> extends DisposableObserver<M> {

    private LoadingDialog mLoadingDialog;

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public RxDataObserver() {

    }

    /**
     * @param dialog may be null,null present not dialog show
     */
    public RxDataObserver(@Nullable LoadingDialog dialog) {
        mLoadingDialog = dialog;
    }

    @Override
    protected void onStart() {
        showLoadingDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
            onFailure(msg);
        } else {
            onFailure(throwable.getMessage());
        }
        dismissLoadingDialog();
    }

    /**
     * 直接返回数据给上层
     */
    @Override
    public void onNext(M m) {
        onSuccess(m);
    }

    @Override
    public void onComplete() {
        dismissLoadingDialog();
    }

    public void showLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {

        if (mLoadingDialog != null) {
            Activity activity = mLoadingDialog.getContext();
            if (isInvalidContext(activity)) {
                return;
            }
            mLoadingDialog.hide();
        }
    }

    private boolean isInvalidContext(Activity activity) {
        if (activity == null) {
            return false;
        }
        return (activity.isDestroyed() || activity.isFinishing());
    }
}
