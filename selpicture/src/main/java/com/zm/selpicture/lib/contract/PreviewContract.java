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

        void setTvTitle(String title); //设置标题

        void isOri(boolean checked);  //是否是原图

        void setOriText(String size); //原图大小

        void setSelect(boolean checked);//是否选中

        void selMaxError(int maxSelectNum);//最大选中错误

        void setDoneText(int size, int maxSelectNum);//完成选择

        void hideStatusBar(); //隐藏bar

        void showStatusBar();//显示bar

        void onFinish();//关闭当前界面
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
}
