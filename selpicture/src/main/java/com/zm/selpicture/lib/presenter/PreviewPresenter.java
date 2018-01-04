package com.zm.selpicture.lib.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.zm.picture.lib.entity.LocalMedia;
import com.zm.selpicture.lib.contract.PreviewContract;
import com.zm.selpicture.lib.entity.PreviewParam;
import com.zm.selpicture.lib.entity.PreviewResult;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 内容:图片详情P
 * 日期:2018/1/1
 * 创建人:scala
 */
public class PreviewPresenter extends BaseMvpPresenter<PreviewContract.IView> implements PreviewContract.IPresenter {
    private int maxSelectNum;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();
    private boolean isShowBar = true;
    private boolean isOri = false;
    private FragmentActivity context;

    /**
     * 初始化数据
     *
     * @param context
     */
    @Override
    public void onCreate(FragmentActivity context) {
        this.context = context;
        PreviewParam previewParam = (PreviewParam) context.getIntent().getSerializableExtra(ImagePresenter.PARAM);
        maxSelectNum = previewParam.getMaxSelectNum();
        isOri = previewParam.isOri();
        images = previewParam.getImages();
        selectImages = previewParam.getSelectImages();
        int position = previewParam.getPosition();
        getMvpView().setTvTitle((position + 1) + "/" + images.size());
        getMvpView().isOri(isOri);
        onImageSwitch(position);
        getMvpView().setDoneText(selectImages.size(), maxSelectNum);
        getMvpView().setData(images, position);

    }

    /**
     * 图片选中改变
     *
     * @param position 当前选中图片的位置
     */
    @Override
    public void onPageSelected(int position) {
        getMvpView().setTvTitle((position + 1) + "/" + images.size());
        onImageSwitch(position);
    }

    private void onImageSwitch(int position) {
        getMvpView().setSelect(isSelected(images.get(position)));
        isOri(position);
    }

    /**
     * 计算原图的大小 是否显示图片大小
     *
     * @param position
     */
    private void isOri(int position) {
        if (isOri) {
            File file = new File(images.get(position).getPath());
            if (file.exists() && file.length() > 0) {
                getMvpView().setOriText(getFileSize(file.length()));
            } else {
                getMvpView().setOriText("");
            }
        } else {
            getMvpView().setOriText("");
        }
    }


    /**
     * 当前图片是否勾选
     *
     * @param image
     * @return
     */
    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是原图
     *
     * @param isOri
     * @param position
     */
    @Override
    public void setIsOri(boolean isOri, int position) {
        this.isOri = isOri;
        isOri(position);
    }

    /**
     * 点击当前图片 是否勾选图片
     *
     * @param isChecked 是否勾选
     * @param position  当前图片位置
     */
    @Override
    public void checkClick(boolean isChecked, int position) {
        if (selectImages.size() >= maxSelectNum && isChecked) {
            getMvpView().selMaxError(maxSelectNum);
            getMvpView().setSelect(false);
            return;
        }
        LocalMedia image = images.get(position);
        if (isChecked) {
            selectImages.add(image);
        } else {
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    break;
                }
            }
        }
        getMvpView().setDoneText(selectImages.size(), maxSelectNum);
    }

    /**
     * 是否显示导航栏
     */
    private void switchBarVisibility() {
        if (isShowBar) {
            getMvpView().hideStatusBar();
        } else {
            getMvpView().showStatusBar();
        }
        isShowBar = !isShowBar;
    }


    /**
     * 完成选择
     *
     * @param isDone 是否完成选择
     */
    @Override
    public void onDoneClick(boolean isDone) {
        Intent intent = new Intent();
        intent.putExtra(ImagePresenter.RESULT, new PreviewResult(isDone, isOri, selectImages));
        context.setResult(Activity.RESULT_OK, intent);
        getMvpView().onFinish();
    }

    /**
     * 获取原图大小
     *
     * @param size
     * @return
     */
    public String getFileSize(long size) {
        StringBuilder strSize = new StringBuilder();
        if (size < 1024) {
            strSize.append(size).append("B");
        } else if (size < 1024 * 1024) {
            strSize.append(size / 1024).append("K");
        } else {
            strSize.append(size / 1024 / 1024).append("M");
        }
        return strSize.toString();
    }

    /**
     * 直接跳转actiivty
     *
     * @param context
     * @param cls
     * @param param
     */
    public static void open(Activity context, Class<?> cls, PreviewParam param, int requestCode) {
        context.startActivityForResult(getOpenIntent(context, cls, param), requestCode);
    }

    /**
     * 直接跳转actiivty
     *
     * @param context
     * @param cls
     * @param param
     */
    public static void open(Context context, Class<?> cls, PreviewParam param) {
        context.startActivity(getOpenIntent(context, cls, param));
    }

    /**
     * 跳转到当前详情界面
     *
     * @param context
     * @param cls     activity.class
     * @param param   请求的参数
     * @return 返回要跳转的intent
     */
    public static Intent getOpenIntent(Context context, Class<?> cls, PreviewParam param) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(ImagePresenter.PARAM, param);
        return intent;
    }

    @Subscriber(tag = "onViewTap")
    private void onViewTap(Intent intent) {
        if (getMvpView() != null)
            switchBarVisibility();
    }
}
