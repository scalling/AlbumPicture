package com.zm.selpicture.lib.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.zm.picture.lib.TakePhoto;
import com.zm.picture.lib.entity.TResult;
import com.zm.selpicture.lib.entity.PreviewParam;
import com.zm.selpicture.lib.ui.adapter.ImageListAdapter;
import com.zm.selpicture.lib.ui.popup.FolderPopup;

/**
 * Created by shake on 2017/8/29.
 * 获取本地图片
 */

public interface ImageContract {
    interface IView {

        /**
         * 返回信息 关闭当前activity 反出数据 选中的多个图片
         */
        void onFinish();

        /**
         * 图片当前列表名称
         *
         * @param name 名称
         */
        void setFolderName(String name);

        /**
         * 多选错误返回
         *
         * @param maxSize 最大选中图片个数
         */
        void onMaxError(int maxSize);

        /**
         * @param flag 是否是多选
         */
        void setRestVisibility(boolean flag);

        /**
         * @param flag 是否显示预览
         */
        void setPreviewVisibility(boolean flag);

        /**
         * 绑定adapter
         *
         * @param adapter 图片列表
         */
        void bindAdapter(ImageListAdapter adapter);

        /**
         * @param doneEnable 当前是否有图片选中
         * @param selSize    当前选中图片图少个
         * @param maxSize    最大选择的图片个数
         */
        void checkSel(boolean doneEnable, int selSize, int maxSize);

        /**
         * activity跳转 跳转到详情
         *
         * @param previeParam 详情的请求参数
         */
        void setPrevieParam(PreviewParam previeParam);

        /**
         * 初始化拍照
         *
         * @param listener 拍照的回调
         * @return
         */
        TakePhoto getTakePhoto(TakePhoto.TakeResultListener listener);

        /**
         * 裁剪失败
         *
         * @param result
         * @param msg
         */
        void takeFail(TResult result, String msg);

        /**
         * 裁剪取消
         */
        void takeCancel();

        /**
         * 初始化图片库选择弹框
         *
         * @return
         */
        FolderPopup getFolderPopup();

        /**
         * 点击拍照及请求权限
         */
        void openCamera();
    }

    interface IPresenter {
        void initParams(FragmentActivity context);

        void loadData(FragmentActivity context); //获取所有本地图片地址

        void onDoneClick();//完成选择

        void startSelPreview();//预览

        void onActivityResult(int requestCode, int resultCode, Intent data);//回调

        void showFolderPopup(View view);//显示弹框

        void clickCamera();//点击拍照


    }
}
