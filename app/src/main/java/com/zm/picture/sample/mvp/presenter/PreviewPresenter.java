package com.zm.picture.sample.mvp.presenter;

import android.app.Activity;
import android.content.Intent;


import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.sample.TConstant;
import com.zm.picture.sample.base.BaseMvpPresenter;
import com.zm.picture.sample.mvp.contract.PreviewContract;
import com.zm.picture.sample.mvp.model.PreviewModel;
import com.zm.picture.sample.mvp.ui.adapter.ImagePreviewPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shake on 2017/8/31.
 */

public class PreviewPresenter extends BaseMvpPresenter<PreviewContract.IView> implements PreviewContract.IPresenter{
    private int maxSelectNum;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();
    private boolean isShowBar = true;
    private boolean isOri = false;
    private PreviewModel model;

    public PreviewPresenter(){
        model = new PreviewModel();
    }
    @Override
    public void onCreate() {
        maxSelectNum = getMvpView().getActivity().getIntent().getIntExtra(TConstant.IMAGE_MAX, TConstant.IMAGE_MAX_SIZE);
        boolean enablePreview = getMvpView().getActivity().getIntent().getBooleanExtra(TConstant.IMAGE_ENABLE_PREVIEW, false);
        isOri = getMvpView().getActivity().getIntent().getBooleanExtra(TConstant.IS_ORI, false);
        images = (List<LocalMedia>) getMvpView().getActivity().getIntent().getSerializableExtra(TConstant.IMAGE_PREVIEW_LIST);
        selectImages = (List<LocalMedia>) getMvpView().getActivity().getIntent().getSerializableExtra(TConstant.IMAGE_PREVIEW_SELECT_LIST);
        int position = getMvpView().getActivity().getIntent().getIntExtra(TConstant.IMAGE_PREVIEW_POSITION, 1);
        getMvpView().setTvTitle((position + 1) + "/" + images.size());
        if (isOri) {
            getMvpView().isOri(true);
        } else {
            getMvpView().isOri(false);
        }
        onImageSwitch(position);
        getMvpView().setDoneText(selectImages.size(),maxSelectNum);
        getMvpView().setAdapter(new ImagePreviewPagerAdapter(getMvpView().getActivity().getSupportFragmentManager(),images),position);
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
                getMvpView().setOriText(getMvpView().getOriSizeText(model.getFileSize(file.length())));
            } else {
                getMvpView().setOriText(getMvpView().getOriSizeText(""));
            }
        } else {
            getMvpView().setOriText(getMvpView().getOriSizeText(""));
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
    public void setIsOri(boolean isOri,int position) {
        this.isOri = isOri;
        isOri(position);
    }

    @Override
    public void checkClick(boolean isChecked,int position) {
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
        getMvpView().setDoneText(selectImages.size(),maxSelectNum);
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
        intent.putExtra(TConstant.REQUEST_OUTPUT, (ArrayList)selectImages);
        intent.putExtra(TConstant.OUTPUT_ISDONE, isDone);
        intent.putExtra(TConstant.IS_ORI, isOri);
        getMvpView().getActivity().setResult(Activity.RESULT_OK, intent);
        getMvpView().onFinish();
    }
}
