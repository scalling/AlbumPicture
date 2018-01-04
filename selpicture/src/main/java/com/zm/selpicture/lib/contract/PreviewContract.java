package com.zm.selpicture.lib.contract;

import android.support.v4.app.FragmentActivity;

import com.zm.selpicture.lib.ui.adapter.ImagePreviewPagerAdapter;


/**
 * Created by shake on 2017/8/31.
 * 图片显示
 */

public interface PreviewContract {
    interface IView {
        void setAdapter(ImagePreviewPagerAdapter adapter, int selPosition);

        /**
         * 设置标题
         *
         * @param title 标题
         */
        void setTvTitle(String title);

        /**
         * 是否是原图
         *
         * @param checked
         */
        void isOri(boolean checked);

        /**
         * @param size 原图大小
         */

        void setOriText(String size);

        /**
         * @param checked 是否选中
         */
        void setSelect(boolean checked);

        /**
         * 最大选中错误
         *
         * @param maxSelectNum 最大图片个数
         */
        void selMaxError(int maxSelectNum);

        /**
         * 完成选择
         *
         * @param size         当前选中的图片个数
         * @param maxSelectNum 最大图片个数
         */
        void setDoneText(int size, int maxSelectNum);

        /**
         * 隐藏bar
         */
        void hideStatusBar();

        /**
         * 显示bar
         */
        void showStatusBar();

        /**
         * 关闭当前界面
         */
        void onFinish();
    }

    interface IPresenter {
        //初始化数据
        void onCreate(FragmentActivity context);

        //数据选中改变
        void onPageSelected(int position);

        //设置是否是原图
        void setIsOri(boolean isOri, int position);

        //设置是否选中
        void checkClick(boolean isCheck, int position);

        //完成选择
        void onDoneClick(boolean isDone);
    }
}
