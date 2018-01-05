package com.zm.albumpic.upgrade.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zm.albumpic.upgrade.entity.PreviewParam;
import com.zm.albumpic.upgrade.ui.popup.FolderPopup;
import com.zm.albumpic.TakePhoto;
import com.zm.albumpic.entity.LocalMedia;
import com.zm.albumpic.entity.LocalMediaFolder;
import com.zm.albumpic.entity.TResult;

import java.util.List;

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
        void bindAdapter(RecyclerView.Adapter adapter);


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

        /**
         * 设置图片数据
         *
         * @param datas 图片数据
         */
        void setData(List<LocalMedia> datas);//图片数据

        /**
         * 设置文件夹数据
         *
         * @param folders 文件夹数据
         */
        void setFolder(List<LocalMediaFolder> folders);
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
