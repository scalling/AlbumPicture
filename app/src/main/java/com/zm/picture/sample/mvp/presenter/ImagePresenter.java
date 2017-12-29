package com.zm.picture.sample.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;


import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.lib.util.LocalMediaLoader;
import com.zm.picture.sample.TConstant;
import com.zm.picture.sample.base.BaseMvpPresenter;
import com.zm.picture.sample.mvp.contract.ImageContract;
import com.zm.picture.sample.mvp.ui.activity.ImagePreviewActivity;
import com.zm.picture.sample.mvp.ui.adapter.ImageListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shake on 2017/8/29.
 */

public class ImagePresenter extends BaseMvpPresenter<ImageContract.IView> implements ImageContract.IPresenter {
    private int maxSelectNum = 9;//默认最多选择9张
    private boolean isMultiple = false;//是否多选
    private boolean enablePreview = false;//详情是否可用
    private boolean isOri = false;//是否压缩
    private List<LocalMedia> selectImages = new ArrayList<LocalMedia>();
    private ImageListAdapter imageAdapter;

    @Override
    public void onCreate() {
         maxSelectNum = getMvpView().getActivity().getIntent().getIntExtra(TConstant.IMAGE_MAX, TConstant.IMAGE_MAX_SIZE);
        enablePreview = getMvpView().getActivity().getIntent().getBooleanExtra(TConstant.IMAGE_ENABLE_PREVIEW, false);
       if(maxSelectNum>1)
           isMultiple=true;
        if (!isMultiple) {
            enablePreview = false;
        }

        getMvpView().setRestVisibility(isMultiple ? true : false);
        getMvpView().setPreviewVisibility(enablePreview ? true : false);
        imageAdapter = new ImageListAdapter(getMvpView().getActivity(), this);
        getMvpView().bindAdapter(imageAdapter);
    }

    @Override
    public boolean isMultiple() {
        return isMultiple;
    }
    @Override
    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getFolders(FragmentActivity activity) {
        new LocalMediaLoader(getMvpView().getActivity(), LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                getMvpView().setFolders(folders);
                getMvpView().setFolderName(folders.get(0).getName());
                imageAdapter.setDataList(folders.get(0).getImages());
                imageAdapter.notifyDataSetChanged();
            }
        });
    }



    @Override
    public void onDoneClick() {
        ArrayList<String> images = new ArrayList<>();
        for (LocalMedia media : selectImages) {
            images.add(media.getPath());
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(TConstant.REQUEST_OUTPUT,images);
        intent.putExtra(TConstant.IS_ORI, isOri);
        getMvpView().getActivity().setResult(Activity.RESULT_OK, intent);
        getMvpView().onFinish();
    }



    @Override
    public void checkBoxClick(boolean isChecked, LocalMedia image) {
        if (!enablePreview) {
            return;
        }
        if (selectImages.size() >= maxSelectNum && !isChecked) {
            getMvpView().onMaxError(maxSelectNum);
            return;
        }
        if (isChecked) {
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    break;
                }
            }
        } else {
            selectImages.add(image);
        }
        imageAdapter.notifyDataSetChanged();
        checkSel();
    }

    @Override
    public void selectFolderImages(String name, List<LocalMedia> images) {
        getMvpView().setFolderName(name);
        imageAdapter.setDataList(images);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void startSelPreview() {

          Intent intent = previewShow(selectImages, selectImages, 0, isOri, maxSelectNum);
          intent.setClass(getMvpView().getActivity(), ImagePreviewActivity.class);
          getMvpView().getActivity().startActivityForResult(intent,TConstant.REQUEST_PREVIEW);
    }

    @Override
    public void onItemClick(List<LocalMedia> images, LocalMedia media, int position, boolean isChecked) {
        if (!isMultiple() || enablePreview) {
            if (enablePreview) {
                Intent intent = previewShow(images, selectImages, position, isOri, maxSelectNum);
                intent.setClass(getMvpView().getActivity(), ImagePreviewActivity.class);
                getMvpView().getActivity().startActivityForResult(intent,TConstant.REQUEST_PREVIEW);
            } else {
                if (media != null){
                    selectImages.add(media);
                    onDoneClick();
                }
            }
        } else {
            checkBoxClick(isChecked, media);
        }
    }

    private void checkSel() {
        boolean enable = selectImages.size() != 0;
        getMvpView().setSelEnable(enable);
        String previewText, restsText;
        if (enable) {
            previewText = getMvpView().getPreviewTextData(selectImages.size());
            restsText = getMvpView().getTvRestData(selectImages.size(), maxSelectNum);
        } else {
            previewText = getMvpView().getPreviewTextNull();
            restsText = getMvpView().getTvRestNull();
        }
        getMvpView().setPreviewText(previewText);
        getMvpView().setTvRestText(restsText);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == TConstant.REQUEST_PREVIEW) {
            boolean isDone = data.getBooleanExtra(TConstant.OUTPUT_ISDONE, false);
            this.selectImages = (List<LocalMedia>) data.getSerializableExtra(TConstant.REQUEST_OUTPUT);
            this.isOri = data.getBooleanExtra(TConstant.IS_ORI, false);
            if (isDone) {
                onDoneClick();
            } else {
                checkSel();
            }
        }
    }


    public Intent previewShow(List<LocalMedia> images, List<LocalMedia> selImage, int position, boolean isOri, int maxSelectNum){
        Intent intent = new Intent();
        intent.putExtra(TConstant.IMAGE_PREVIEW_LIST, (Serializable) images);
        intent.putExtra(TConstant.IMAGE_PREVIEW_SELECT_LIST, (Serializable) selImage);
        intent.putExtra(TConstant.IMAGE_PREVIEW_POSITION, position);
        intent.putExtra(TConstant.IS_ORI, isOri);
        intent.putExtra(TConstant.IMAGE_MAX, maxSelectNum);
        return intent;
    }
}
