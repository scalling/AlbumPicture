package com.zm.selpicture.lib.ui.popup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zm.picture.lib.TakePhoto;
import com.zm.picture.lib.TakePhotoImpl;
import com.zm.picture.lib.entity.CropOptions;
import com.zm.picture.lib.util.TFileUtils;
import com.zm.selpicture.lib.R;

import java.io.File;


/**
 * 内容:拍照、相册选择弹框
 * 日期:2018/1/1
 * 创建人:scala
 */

public class TackPhotoPopup extends PopupWindow implements View.OnClickListener {
    private View view;
    private View confirmButton;
    private boolean enableCrap;//是否裁剪
    private int outx = 500;//默认裁剪大小
    private int outy = 500;//默认裁剪大小
    protected Activity context;
    private TakePhoto takePhoto;
    private Builder builder;

    private TackPhotoPopup(Activity context, Builder builder) {
        super(context);
        this.context = context;
        this.builder = builder;
        init();
    }

    //初始化布局
    protected void initView() {
        confirmButton = view.findViewById(R.id.btn_camera);
        confirmButton.setOnClickListener(this);
        view.findViewById(R.id.btn_photo).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(builder.getLayout(), null);
        initView();
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContent(view);
        setOutsideTouchable(true);
        takePhoto = new TakePhotoImpl(context, builder.getTackPhotoResult());
        takePhoto.onEnableCompress(null, false);
    }

    public void setContent(View view) {
        setContentView(view);
        setFocusable(true);
        setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(-00000);
        setBackgroundDrawable(dw);
        update();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_camera) {
            clickCamera();
        } else if (v.getId() == R.id.btn_photo) {
            clickPhoto();
        }
        dismiss();
    }

    //裁剪配置
    public CropOptions getCropOptions() {
        return new CropOptions.Builder()
                .setAspectX(outx)
                .setAspectY(outy)
                .setOutputX(outx)
                .setOutputY(outy)
                .setWithOwnCrop(false)
                .create();
    }

    //需要裁剪
    public void showCrap(int outx, int outy) {
        this.enableCrap = true;
        this.outx = outx;
        this.outy = outy;
        showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
    }

    //不需要裁剪
    public void show() {
        this.enableCrap = false;
        showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
    }

    //点击拍照
    public void clickCamera() {
        camera();
    }

    //点击相册
    public void clickPhoto() {
        photo();
    }

    //拍照
    protected void camera() {
        File file = TFileUtils.createCameraFile(context);
        Uri fileUri = Uri.fromFile(file);
        if (enableCrap) {
            takePhoto.onPickFromCaptureWithCrop(fileUri, getCropOptions());
        } else {
            takePhoto.onPickFromCapture(fileUri);
        }
    }

    //相册
    protected void photo() {
        if (enableCrap) {
            takePhoto.onPickMultipleWithCrop(1, getCropOptions());
        } else {
            takePhoto.onPickMultiple(1);
        }
    }

    //回调
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhoto.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 配置文件
     */
    public static final class Builder {
        private int layout = R.layout.tackphoto_popup;//布局view
        private TakePhoto.TakeResultListener tackPhotoResult;//回掉
        private int btnCameraId = R.id.btn_camera;//点击拍照按钮ID
        private int btnPhotoId = R.id.btn_photo;//点击相册按钮ID
        private int btnCancelId = R.id.btn_cancel;//点击取消ID

        public Builder setLayout(int layout) {
            this.layout = layout;
            return this;
        }

        public Builder setLayout(int layout, int btnCameraId, int btnPhotoId, int btnCancelId) {
            this.layout = layout;
            this.btnCameraId = btnCameraId;
            this.btnPhotoId = btnPhotoId;
            this.btnCancelId = btnCancelId;
            return this;
        }

        public Builder setTackPhotoResult(TakePhoto.TakeResultListener tackPhotoResult) {
            this.tackPhotoResult = tackPhotoResult;
            return this;
        }

        public int getBtnCameraId() {
            return btnCameraId;
        }

        public int getBtnPhotoId() {
            return btnPhotoId;
        }

        public int getBtnCancelId() {
            return btnCancelId;
        }

        public int getLayout() {
            return layout;
        }

        public TakePhoto.TakeResultListener getTackPhotoResult() {
            return tackPhotoResult;
        }

        public TackPhotoPopup build(Activity context) {
            return new TackPhotoPopup(context, this);
        }
    }

}
