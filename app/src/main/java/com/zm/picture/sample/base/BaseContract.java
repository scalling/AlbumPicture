package com.zm.picture.sample.base;

import android.support.annotation.NonNull;

/**
 * Created by shake on 2017/8/29.
 */

public class BaseContract {
    public interface IBaseView {
    }

    public interface IBaseModel {
    }

    public interface IBasePresenter<V> {
        /**
         * 绑定接口
         */
        void attachView(V view);

        /**
         * 释放接口
         */
        void detachView();
    }

    public interface IBaseDelegate<V extends IBaseView, P extends IBasePresenter<V>> {

        /**
         * 初始化presenter
         */
        @NonNull
        P createPresenter();

        /**
         * 获取presenter
         */
        @NonNull
        P getPresenter();

    }

//    interface IView extends BaseContract.IBaseView {}
//    interface IPresenter extends BaseContract.IBasePresenter {}
//    interface IModel extends BaseContract.IBaseModel {}
}
