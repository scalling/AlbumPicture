package com.zm.albumpic.upgrade.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zm.albumpic.TakePhoto;
import com.zm.albumpic.entity.CropOptions;
import com.zm.albumpic.entity.LocalMedia;
import com.zm.albumpic.entity.LocalMediaFolder;
import com.zm.albumpic.entity.TContextWrap;
import com.zm.albumpic.entity.TImage;
import com.zm.albumpic.entity.TResult;
import com.zm.albumpic.util.LocalMediaLoader;
import com.zm.albumpic.util.TConstant;
import com.zm.albumpic.util.TFileUtils;
import com.zm.albumpic.upgrade.R;
import com.zm.albumpic.upgrade.contract.ImageContract;
import com.zm.albumpic.upgrade.entity.ImageParam;
import com.zm.albumpic.upgrade.entity.PreviewParam;
import com.zm.albumpic.upgrade.entity.PreviewResult;
import com.zm.albumpic.upgrade.ui.adapter.ImageListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 内容:图片列表P
 * 日期:2018/1/1
 * 创建人:scala
 */
public class ImagePresenter extends BaseMvpPresenter<ImageContract.IView> implements ImageContract.IPresenter, ImageListAdapter.ImageListInterface {
    public final static String PARAM = "param";//请求的参数
    public final static String RESULT = "result";//请求的参数
    private boolean isOri = false;//是否压缩
    private List<LocalMedia> selectImages = new ArrayList<LocalMedia>();//选中的图片
    private RecyclerView.Adapter adapter;
    private FragmentActivity context;
    private ImageParam param;//请求的参数
    private TakePhoto takePhoto;

    public ImagePresenter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 初始化传输参数
     *
     * @param context
     */
    @Override
    public void initParams(FragmentActivity context) {
        if (param == null) {
            this.context = context;
            param = (ImageParam) context.getIntent().getSerializableExtra(PARAM);
            initPhoto();
            getMvpView().setRestVisibility(param.isMultiple() ? true : false);
            getMvpView().setPreviewVisibility(param.isEnablePreview() ? true : false);
            getMvpView().bindAdapter(adapter);
        }
    }


    /**
     * 加载本地图片数据
     */
    @Override
    public void loadData(FragmentActivity context) {

        new LocalMediaLoader(context, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                getMvpView().setFolder(folders);
                //设置监听
                if (getMvpView().getFolderPopup() != null) {
                    getMvpView().getFolderPopup().setOnItemClickListener((name, images) -> {
                        getMvpView().getFolderPopup().dismiss();
                        getMvpView().setFolderName(name);
                        setListData(name,images);
                    });
                }
                if (folders.size() > 0) {
                    setListData(folders.get(0).getName(),folders.get(0).getImages());
                } else {
                    getMvpView().setFolderName(context.getString(R.string.all_image));
                }
            }
        });
    }

    private void setListData(String name,List<LocalMedia> data){
        getMvpView().setFolderName(name);
        List<LocalMedia> newData =new ArrayList<>();
        newData.addAll(data);
        if (param.isShowCamera()) {
            newData.add(0, new LocalMedia(""));
        }
        getMvpView().setData(newData);
        adapter.notifyDataSetChanged();
    }

    /**
     * 当前图片是否选中
     *
     * @param image 对比的数据
     * @return
     */
    @Override
    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 完成选择
     */
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

    /**
     * 复选框点击
     *
     * @param isChecked 是否勾选
     * @param image     当前点击的图片
     */
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
        adapter.notifyDataSetChanged();
    }

    /**
     * 跳转到详情界面
     */
    @Override
    public void startSelPreview() {
        getMvpView().setPrevieParam(new PreviewParam(param.getMaxSelectNum(), isOri, 0, selectImages, selectImages));
    }

    /**
     * 图片点击
     *
     * @param images   当前选中的文件夹的所有图片
     * @param media    点击的图片
     * @param position 图片的位置
     * @param viewType 点击的item类型
     */
    @Override
    public void onItemClick(List<LocalMedia> images, LocalMedia media, int position, int viewType) {
        if (viewType == ImageListAdapter.TYPE_CAMERA) {
            getMvpView().openCamera();
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

    /**
     * 拍照
     */
    @Override
    public void clickCamera() {
        if (takePhoto == null) return;
        File file = TFileUtils.createCameraFile(context);
        Uri fileUri = Uri.fromFile(file);
        if (param.isEnableCrap()) {
            takePhoto.onPickFromCaptureWithCrop(fileUri, getCropOptions());
        } else {
            takePhoto.onPickFromCapture(fileUri);
        }
    }

    @Override
    public boolean isShowCamera() {
        return param.isShowCamera();
    }

    @Override
    public boolean isMultiple() {
        return param.isMultiple();
    }

    /**
     * 裁剪配置
     *
     * @return
     */
    private CropOptions getCropOptions() {
        return new CropOptions.Builder()
                .setAspectX(param.getOutx())
                .setAspectY(param.getOuty())
                .setOutputX(param.getOutx())
                .setOutputY(param.getOuty())
                .setWithOwnCrop(false)
                .create();
    }

    /**
     * 图片选择
     */
    private void checkSel() {
        boolean enable = selectImages.size() != 0;
        getMvpView().checkSel(enable, selectImages.size(), param.getMaxSelectNum());
        adapter.notifyDataSetChanged();
    }

    /**
     * 后个界面的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * 初始化拍照
     */
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
                    getMvpView().takeFail(result, msg);
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


    /**
     * 显示选择图片文件夹弹框
     *
     * @param view
     */
    @Override
    public void showFolderPopup(View view) {
        if (getMvpView().getFolderPopup() == null) return;
        if (getMvpView().getFolderPopup().isShowing()) {
            getMvpView().getFolderPopup().dismiss();
        } else {
            getMvpView().getFolderPopup().showAsDropDown(view);
        }
    }

    /**
     * 直接跳转actiivty
     *
     * @param context
     * @param cls
     * @param param
     */
    public static void open(Activity context, Class<?> cls, ImageParam param, int requestCode) {
        context.startActivityForResult(getOpenIntent(context, cls, param), requestCode);
    }

    /**
     * @param context
     * @param cls     要跳转的activity
     * @param param   跳转到图片列表请求的参数
     * @return 返回要跳转的intent
     */
    public static Intent getOpenIntent(Context context, Class<?> cls, ImageParam param) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(PARAM, param);
        return intent;
    }


}
