package com.zm.selpicture.lib.contract;

import android.support.annotation.NonNull;

/**
 * Created by shake on 2017/8/29.
 */

public class BaseContract {
    public interface IBasePresenter<V> {
        void attachView(V view);//绑定接口

        void detachView();//释放接口
    }
}
