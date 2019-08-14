package com.sonicers.commonlib.mvp;


import android.annotation.SuppressLint;

import com.sonicers.commonlib.rx.RxSchedulers;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


public class BasePresenter<V extends BaseView> {
    public V iView;
    private CompositeDisposable mCompositeDisposable;

    /**
     * 数据请求
     */
    @SuppressLint("CheckResult")
    public void addSubscriptionStartRequest(Observable observable, DisposableObserver observer) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(observer);
        observable.compose(RxSchedulers.io_main())
                .subscribeWith(observer);
    }

    /**
     * V与P形成依赖
     */
    public void attachView(V mvpView) {
        this.iView = mvpView;
    }

    /**
     * P释放V
     */
    public void detachView() {
        this.iView = null;
        onUnSubscribe();
    }

    //RxJava取消注册，以避免内存泄露
    public void onUnSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

}
