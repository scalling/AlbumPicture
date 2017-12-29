package com.zm.picture.sample.base;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by shake on 2017/8/30.
 */

public abstract class BaseMVPAppCompatActivity <V extends BaseContract.IBaseView, P extends BaseMvpPresenter<V>> extends
        BaseAppCompatActivity implements BaseContract.IBaseDelegate<V, P> {
    protected P mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }
    @NonNull
    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
