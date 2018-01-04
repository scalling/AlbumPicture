package com.zm.selpicture.lib.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.zm.picture.lib.TakePhoto;
import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.lib.entity.TResult;
import com.zm.selpicture.lib.entity.PreviewParam;
import com.zm.selpicture.lib.ui.adapter.ImageListAdapter;
import com.zm.selpicture.lib.ui.popup.FolderPopup;

import java.util.List;

/**
 * Created by shake on 2017/8/29.
 * 获取本地图片
 */

public interface ImageContract {
    interface IView {

        void onFinish();//返回信息 关闭当前activity 反出数据 选中的多个图片

        void setFolderName(String name);//图片当前列表名称

        void onMaxError(int maxSize);  //多选错误返回

        void setRestVisibility(boolean flag);//是否是多选

        void setPreviewVisibility(boolean flag);//是否显示预览

        void bindAdapter(ImageListAdapter adapter);//绑定adapter
        /**
         *
         * @param doneEnable  当前是否有图片选中
         * @param selSize 当前选中图片图少个
         * @param maxSize 最大选择的图片个数
         */
        void checkSel(boolean doneEnable,int selSize,int maxSize);

        void setPrevieParam(PreviewParam previeParam);//activity跳转

        TakePhoto getTakePhoto(TakePhoto.TakeResultListener listener);//初始化拍照

        void takeFail(TResult result, String msg);//裁剪失败

        void takeCancel();//裁剪取消

        FolderPopup getFolderPopup();//初始化弹框

        void openCamera();//打开相机及权限
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
