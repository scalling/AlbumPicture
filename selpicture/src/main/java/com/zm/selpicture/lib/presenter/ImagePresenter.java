package com.zm.selpicture.lib.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.lib.util.LocalMediaLoader;
import com.zm.picture.lib.util.TConstant;
import com.zm.selpicture.lib.contract.ImageContract;
import com.zm.selpicture.lib.entity.ImageParam;
import com.zm.selpicture.lib.entity.PreviewParam;
import com.zm.selpicture.lib.entity.PreviewResult;
import com.zm.selpicture.lib.ui.adapter.ImageListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shake on 2017/8/29.
 */

public class ImagePresenter extends BaseMvpPresenter<ImageContract.IView> implements ImageContract.IPresenter, ImageListAdapter.ImageListInterface {
    public final static String PARAM = "param";//请求的参数
    public final static String RESULT = "result";//请求的参数
    private int maxSelectNum = 0;//默认最多选择9张
    private boolean isMultiple = false;//是否多选
    private boolean enablePreview = false;//详情是否可用
    private boolean isOri = false;//是否压缩
    private List<LocalMedia> selectImages = new ArrayList<LocalMedia>();
    private ImageListAdapter imageAdapter;
    private FragmentActivity context;


    @Override
    public void loadData(FragmentActivity context) {
        if (imageAdapter == null) {
            this.context = context;
            ImageParam param = (ImageParam) context.getIntent().getSerializableExtra(PARAM);
            maxSelectNum = param.getMaxSelectNum();
            enablePreview = param.isEnablePreview();
            if (maxSelectNum > 1)
                isMultiple = true;
            if (!isMultiple) {
                enablePreview = false;
            }
            getMvpView().setRestVisibility(isMultiple ? true : false);
            getMvpView().setPreviewVisibility(enablePreview ? true : false);
            imageAdapter = new ImageListAdapter(context, this);
            getMvpView().bindAdapter(imageAdapter);
        }
        new LocalMediaLoader(context, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                getMvpView().bindFolder(folders);
                getMvpView().setFolderName(folders.get(0).getName());
                imageAdapter.setDataList(folders.get(0).getImages());
                imageAdapter.notifyDataSetChanged();
            }
        });
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
    public boolean isMultiple() {
        return isMultiple;
    }

    @Override
    public void onDoneClick() {
        ArrayList<String> images = new ArrayList<>();
        for (LocalMedia media : selectImages) {
            images.add(media.getPath());
        }
        Intent intent = new Intent();
        intent.putExtra(TConstant.REQUEST_OUTPUT, images);
        context.setResult(Activity.RESULT_OK, intent);
        getMvpView().onFinish();
    }

    @Override
    public void checkBoxClick(boolean isChecked, LocalMedia image) {

        if (!isMultiple) {
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
        checkSel();
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectFolderImages(String name, List<LocalMedia> images) {
        getMvpView().setFolderName(name);
        imageAdapter.setDataList(images);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void startSelPreview() {
        getMvpView().setPrevieParam(new PreviewParam(maxSelectNum, isOri, 0, selectImages, selectImages));
    }

    @Override
    public void onItemClick(List<LocalMedia> images, LocalMedia media, int position, boolean isChecked) {
        if (!isMultiple || enablePreview) {
            if (enablePreview) {
                getMvpView().setPrevieParam(new PreviewParam(maxSelectNum, isOri, position, images, selectImages));
            } else {
                if (media != null) {
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
        getMvpView().setPreviewText(selectImages.size());
        getMvpView().setTvRestText(selectImages.size(), maxSelectNum);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.getSerializableExtra(RESULT) == null)
                return;
            PreviewResult result = (PreviewResult) data.getSerializableExtra(RESULT);
            boolean isDone = result.isDone();
            this.selectImages = result.getImages();
            this.isOri = result.isOri();
            if (isDone) {
                onDoneClick();
            } else {
                checkSel();
            }
        }
    }

    //直接跳转actiivty
    public static void open(Activity context, Class<?> cls, ImageParam param, int requestCode) {
        context.startActivityForResult(getOpenIntent(context, cls, param), requestCode);
    }

    //获取需要跳转的参数
    public static Intent getOpenIntent(Context context, Class<?> cls, ImageParam param) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(PARAM, param);
        return intent;
    }
}
