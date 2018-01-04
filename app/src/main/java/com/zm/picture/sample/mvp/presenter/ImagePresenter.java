package com.zm.picture.sample.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.zm.picture.lib.TakePhoto;
import com.zm.picture.lib.entity.CropOptions;
import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.lib.entity.TContextWrap;
import com.zm.picture.lib.entity.TImage;
import com.zm.picture.lib.entity.TResult;
import com.zm.picture.lib.util.LocalMediaLoader;
import com.zm.picture.lib.util.TConstant;
import com.zm.picture.lib.util.TFileUtils;
import com.zm.picture.sample.R;
import com.zm.picture.sample.base.BaseMvpPresenter;
import com.zm.picture.sample.mvp.contract.ImageContract;
import com.zm.picture.sample.mvp.model.entity.ImageParam;
import com.zm.picture.sample.mvp.model.entity.PreviewParam;
import com.zm.picture.sample.mvp.model.entity.PreviewResult;
import com.zm.picture.sample.mvp.ui.adapter.ImageListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shake on 2017/8/29.
 */

public class ImagePresenter extends BaseMvpPresenter<ImageContract.IView> implements ImageContract.IPresenter, ImageListAdapter.ImageListInterface {
    public final static String PARAM = "param";//请求的参数
    public final static String RESULT = "result";//请求的参数
    private boolean isOri = false;//是否压缩
    private List<LocalMedia> selectImages = new ArrayList<LocalMedia>();
    private ImageListAdapter imageAdapter;
    private FragmentActivity context;
    private ImageParam param;
    private TakePhoto takePhoto;

    @Override
    public void loadData(FragmentActivity context) {
        if (imageAdapter == null) {
            this.context = context;
            param = (ImageParam) context.getIntent().getSerializableExtra(PARAM);
            initPhoto();
            getMvpView().setRestVisibility(param.isMultiple() ? true : false);
            getMvpView().setPreviewVisibility(param.isEnablePreview() ? true : false);
            imageAdapter = new ImageListAdapter(context, this, param.isMultiple(), param.isShowCamera());
            getMvpView().bindAdapter(imageAdapter);
        }
        new LocalMediaLoader(context, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                getMvpView().bindFolder(folders);
                if (folders.size() > 0) {
                    selectFolderImages(folders.get(0).getName(),folders.get(0).getImages());
                } else {
                    selectFolderImages(context.getString(R.string.all_image),new ArrayList<>());
                }
            }
        });
    }
    @Override
    public void selectFolderImages(String name, List<LocalMedia> images) {
        getMvpView().setFolderName(name);
        List<LocalMedia> newData =images;
        if (param.isShowCamera()) {
            newData.add(0, new LocalMedia(""));
        }
        imageAdapter.setDataList(newData);
        imageAdapter.notifyDataSetChanged();
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

        if (!param.isMultiple()) {
            return;
        }
        if (selectImages.size() >= param.getMaxSelectNum() && !isChecked) {
            getMvpView().onMaxError(param.getMaxSelectNum());
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
    public void startSelPreview() {
        getMvpView().setPrevieParam(new PreviewParam(param.getMaxSelectNum(), isOri, 0, selectImages, selectImages));
    }

    @Override
    public void onItemClick(List<LocalMedia> images, LocalMedia media, int position, int viewType) {

        if (viewType == ImageListAdapter.TYPE_CAMERA) {
            clickCamera();
        } else {
            boolean isChecked = isSelected(media);
            if (param.isEnablePreview()) {
                List<LocalMedia> newImages = new ArrayList<>();
                newImages.addAll(images);
                int newPosition = position;
                if (param.isShowCamera()) {
                    newPosition -= 1;
                    newImages.remove(0);
                }
                getMvpView().setPrevieParam(new PreviewParam(param.getMaxSelectNum(), isOri, newPosition, newImages, selectImages));
                return;
            }
            if (param.isMultiple()) {
                checkBoxClick(isChecked, media);
                return;
            }
            if (media != null) {
                selectImages.add(media);
                onDoneClick();
            }
        }
    }

    public void clickCamera() {
        File file = TFileUtils.createCameraFile(context);
        Uri fileUri = Uri.fromFile(file);
        if (param.isEnableCrap()) {
            takePhoto.onPickFromCaptureWithCrop(fileUri, getCropOptions());
        } else {
            takePhoto.onPickFromCapture(fileUri);
        }
    }

    private CropOptions getCropOptions() {
        return new CropOptions.Builder()
                .setAspectX(param.getOutx())
                .setAspectY(param.getOuty())
                .setOutputX(param.getOutx())
                .setOutputY(param.getOuty())
                .setWithOwnCrop(false)
                .create();
    }

    private void checkSel() {
        boolean enable = selectImages.size() != 0;
        getMvpView().setSelEnable(enable);
        getMvpView().setPreviewText(selectImages.size());
        getMvpView().setTvRestText(selectImages.size(), param.getMaxSelectNum());
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

    //初始化拍照
    private void initPhoto() {
        if (param.isShowCamera()) {
            takePhoto = getMvpView().getTakePhoto(new TakePhoto.TakeResultListener() {
                @Override
                public void takeSuccess(TResult result) {
                    String url = result.getImage().getOriginalPath();
                    //(String.format("得到的图片地址：%s %s %s %s", result.getImages().size(), url, result.getImage().getOriginalPath(), result.getImage().getFromType()));
                    ArrayList<String> images = new ArrayList<>();
                    for (TImage dto : result.getImages()) {
                        images.add(dto.getOriginalPath());
                    }
                    Intent intent = new Intent();
                    intent.putExtra(TConstant.REQUEST_OUTPUT, images);
                    context.setResult(Activity.RESULT_OK, intent);
                    getMvpView().onFinish();
                }

                @Override
                public void takeFail(TResult result, String msg) {
                    if (!param.isMultiple())
                        selectImages.clear();
                    getMvpView().takeFail(result,msg);
                }

                @Override
                public void takeCancel() {
                    if (!param.isMultiple())
                        selectImages.clear();
                    getMvpView().takeCancel();
                }

                @Override
                public Intent getPickMultipleIntent(TContextWrap contextWrap, int limit) {
                    return null;
                }
            });
            takePhoto.onEnableCompress(null, false);
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
