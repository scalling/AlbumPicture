package com.zm.selpicture.lib.presenter;


import com.zm.selpicture.lib.contract.BaseContract;

import org.simple.eventbus.EventBus;

public class BaseMvpPresenter<V> implements BaseContract.IBasePresenter<V> {

    private V mMvpView;

    @Override
    public void attachView(V mvpView) {
        if(useEventBus())
            EventBus.getDefault().register(this);
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        if(useEventBus())
            EventBus.getDefault().unregister(this);
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
    public boolean useEventBus() {
        return true;
    }
}
