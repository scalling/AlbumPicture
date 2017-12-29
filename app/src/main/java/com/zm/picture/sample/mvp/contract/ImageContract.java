package com.zm.picture.sample.mvp.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.sample.base.BaseContract;
import com.zm.picture.sample.mvp.ui.adapter.ImageListAdapter;

import java.util.List;

/**
 * Created by shake on 2017/8/29.
 * 获取本地图片
 */

public interface ImageContract {

    interface IView extends BaseContract.IBaseView {
        AppCompatActivity getActivity();
        /**
         * 显示图片 图片地址数据
         */
        void setFolders(List<LocalMediaFolder> folders);
        //返回信息 关闭当前activity 反出数据 选中的多个图片
        void onFinish();
        //获取数据
        void setFolderName(String name);
        //获取选中的数据
        //跳转到下一个地方
        void startPreview(Intent intent, int requestCode);
        //多选错误返回
        void onMaxError(int maxSize);
        //是否是多选
        void setRestVisibility(boolean flag);
        //是否显示预览
        void setPreviewVisibility(boolean flag);
        //完成按钮预览按钮是否可用
        void setSelEnable(boolean enable);
        void bindAdapter(ImageListAdapter adapter);
        //获取文案
         String getTvRestNull();
         String getPreviewTextNull();
         String getTvRestData(int size, int maxSelectNum);
         String getPreviewTextData(int size);
         //设置选中的个数改变文案
         void setTvRestText(String text);
         void setPreviewText(String text);
    }

    interface IPresenter  {
        void onCreate();
        boolean isMultiple();
        //获取所有本地图片地址
        void getFolders(FragmentActivity activity);

        /**
         * 完成选择
         */
        void onDoneClick();
        //图片多选
        void checkBoxClick(boolean isChecked, LocalMedia image);

       //当前图片是否已经被选中
        boolean isSelected(LocalMedia image);

        //设置数据
        void selectFolderImages(String name, List<LocalMedia> images);
        //跳转到下个界面
        void startSelPreview();
        //每项的点击
        void onItemClick(List<LocalMedia> images, LocalMedia media, int position, boolean isChecked);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
