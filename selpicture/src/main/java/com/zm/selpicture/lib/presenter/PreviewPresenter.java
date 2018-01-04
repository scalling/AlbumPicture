package com.zm.selpicture.lib.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.zm.picture.lib.entity.LocalMedia;
import com.zm.selpicture.lib.contract.PreviewContract;
import com.zm.selpicture.lib.entity.ImageParam;
import com.zm.selpicture.lib.entity.PreviewParam;
import com.zm.selpicture.lib.entity.PreviewResult;
import com.zm.selpicture.lib.ui.adapter.ImageListAdapter;
import com.zm.selpicture.lib.ui.adapter.ImagePreviewPagerAdapter;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shake on 2017/8/31.
 */

public class PreviewPresenter extends BaseMvpPresenter<PreviewContract.IView> implements PreviewContract.IPresenter {
    private int maxSelectNum;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();
    private boolean isShowBar = true;
    private boolean isOri = false;
    private FragmentActivity context;
    private ImagePreviewPagerAdapter adapter;

    public PreviewPresenter() {
    }

    public PreviewPresenter(ImagePreviewPagerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onCreate(FragmentActivity context) {
        this.context = context;
        PreviewParam previewParam = (PreviewParam)context.getIntent().getSerializableExtra(ImagePresenter.PARAM);
        maxSelectNum = previewParam.getMaxSelectNum();
        isOri = previewParam.isOri();
        images = previewParam.getImages();
        selectImages = previewParam.getSelectImages();
        int position = previewParam.getPosition();
        getMvpView().setTvTitle((position + 1) + "/" + images.size());
        getMvpView().isOri(isOri);
        onImageSwitch(position);
        getMvpView().setDoneText(selectImages.size(), maxSelectNum);
        if(adapter==null)
            adapter=new ImagePreviewPagerAdapter(context.getSupportFragmentManager(), images);
        getMvpView().setAdapter(adapter, position);
    }
    @Override
    public void onPageSelected(int position) {
        getMvpView().setTvTitle((position + 1) + "/" + images.size());
        onImageSwitch(position);
    }

    private void onImageSwitch(int position) {
        getMvpView().setSelect(isSelected(images.get(position)));
        isOri(position);
    }

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


    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setIsOri(boolean isOri, int position) {
        this.isOri = isOri;
        isOri(position);
    }

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

    @Override
    public void switchBarVisibility() {
        if (isShowBar) {
            getMvpView().hideStatusBar();
        } else {
            getMvpView().showStatusBar();
        }
        isShowBar = !isShowBar;
    }


    @Override
    public void onDoneClick(boolean isDone) {
        Intent intent = new Intent();
        intent.putExtra(ImagePresenter.RESULT,new PreviewResult(isDone,isOri,selectImages));
        context.setResult(Activity.RESULT_OK, intent);
        getMvpView().onFinish();
    }
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
    //直接跳转actiivty
    public static void open(Activity context, Class<?> cls, PreviewParam param, int requestCode) {
        context.startActivityForResult(getOpenIntent(context,cls,param),requestCode);
    }
    //直接跳转actiivty
    public static void open(Context context, Class<?> cls, PreviewParam param) {
        context.startActivity(getOpenIntent(context,cls,param));
    }
    //获取需要跳转的参数
    public static Intent getOpenIntent(Context context, Class<?> cls, PreviewParam param) {
        Intent intent =new Intent(context,cls);
        intent.putExtra(ImagePresenter.PARAM,param);
        return intent;
    }

    @Subscriber(tag = "onViewTap")
    private void onViewTap(Intent intent) {
        if (getMvpView() != null)
            switchBarVisibility();
    }
}
