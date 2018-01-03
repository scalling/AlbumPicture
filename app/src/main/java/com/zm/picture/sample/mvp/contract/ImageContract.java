package com.zm.picture.sample.mvp.contract;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.zm.picture.lib.TakePhoto;
import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.lib.entity.TResult;
import com.zm.picture.sample.base.BaseContract;
import com.zm.picture.sample.mvp.model.entity.PreviewParam;
import com.zm.picture.sample.mvp.ui.adapter.ImageListAdapter;

import java.util.List;

/**
 * Created by shake on 2017/8/29.
 * 获取本地图片
 */

public interface ImageContract {
    interface IView extends BaseContract.IBaseView {
        void bindFolder(List<LocalMediaFolder> folders);//图片类型列表

        void onFinish();//返回信息 关闭当前activity 反出数据 选中的多个图片

        void setFolderName(String name);//图片当前列表名称

        void onMaxError(int maxSize);  //多选错误返回

        void setRestVisibility(boolean flag);//是否是多选

        void setPreviewVisibility(boolean flag);//是否显示预览

        void setSelEnable(boolean enable);//完成按钮预览按钮是否可用

        void bindAdapter(ImageListAdapter adapter);

        void setTvRestText(int size, int maxSelectNum);//当前选中图片图少个

        void setPreviewText(int size);//当前图片文件夹图片个数

        void setPrevieParam(PreviewParam previeParam);//activity跳转

        TakePhoto getTakePhoto(TakePhoto.TakeResultListener listener);//获取拍照

        void takeFail(TResult result, String msg);//裁剪失败

        void takeCancel();//裁剪取消
    }

    interface IPresenter {
        void loadData(FragmentActivity context); //获取所有本地图片地址

        void onDoneClick();//完成选择

        void selectFolderImages(String name, List<LocalMedia> images); //设置数据

        void startSelPreview();//预览

        void onActivityResult(int requestCode, int resultCode, Intent data);//回掉
    }
}
