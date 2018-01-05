package com.zm.picture.sample.mvp.contract;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.zm.picture.sample.base.BaseContract;
import com.zm.picture.sample.mvp.ui.adapter.ImagePreviewPagerAdapter;


/**
 * Created by shake on 2017/8/31.
 * 图片显示
 */

public interface PreviewContract {
     interface IView extends BaseContract.IBaseView {
        void setAdapter(ImagePreviewPagerAdapter adapter, int selPosition);
        //设置标题
        void setTvTitle(String title);
        //是否是原图
        void isOri(boolean checked);
        //原图大小
        void setOriText(String size);
        //是否选中
        void setSelect(boolean checked);
        //最大选中错误
        void selMaxError(int maxSelectNum);
        //完成选择
        void setDoneText(int size, int maxSelectNum);
        //隐藏bar
        void hideStatusBar();
        //显示bar
        void showStatusBar();
        //关闭当前界面
        void onFinish();
    }
     interface IPresenter {
        //初始化数据
        void onCreate(FragmentActivity context);
        //数据选中该拜年
        void onPageSelected(int position);
        //设置是否是原图
        void setIsOri(boolean isOri, int position);
        //设置是否选中
        void checkClick(boolean isCheck, int position);
        //点击是否隐藏bar
        void switchBarVisibility();
        //完成选择
        void onDoneClick(boolean isDone);
    }
     interface IModel extends BaseContract.IBaseModel {
    }
}
