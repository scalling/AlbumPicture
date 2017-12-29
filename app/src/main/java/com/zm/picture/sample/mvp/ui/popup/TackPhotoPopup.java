package com.zm.picture.sample.mvp.ui.popup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zm.picture.lib.TakePhoto;
import com.zm.picture.lib.TakePhotoImpl;
import com.zm.picture.lib.entity.CropOptions;
import com.zm.picture.lib.entity.TContextWrap;
import com.zm.picture.lib.entity.TResult;
import com.zm.picture.lib.util.TFileUtils;
import com.zm.picture.sample.R;
import com.zm.picture.sample.TConstant;
import com.zm.picture.sample.mvp.ui.activity.ImageSelectorActivity;
import com.zm.tool.library.util.Logger;
import com.zm.tool.library.util.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by shake on 2017/8/24.
 */

public class TackPhotoPopup extends PopupWindow implements TakePhoto.TakeResultListener{
    private View view;
    @BindView(R.id.btn_camera)
    Button confirmButton;
    private boolean enableCrap;//是否裁剪
    private int outx = 500;//默认裁剪大小
    private int outy = 500;//默认裁剪大小
    protected Activity context;
    private TackPhotoResult tackPhotoResult;
    private TakePhoto takePhoto;
    public TackPhotoPopup(Activity context, TackPhotoResult tackPhotoResult) {
        super(context);
        this.context = context;
        this.tackPhotoResult = tackPhotoResult;
        init();
    }
    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.tackphoto_popup, null);
        ButterKnife.bind(this, view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContent(view);
        setOutsideTouchable(true);
        takePhoto = new TakePhotoImpl(context,this);
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

    @OnClick({R.id.btn_camera,R.id.btn_photo,R.id.root,R.id.btn_cancel})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_camera:
                camera();
                break;
            case R.id.btn_photo:
                photo();
                break;
        }
        dismiss();
    }

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


    private void camera() {

        File file = TFileUtils.createCameraFile(context);
        Uri fileUri = Uri.fromFile(file);
        if (enableCrap) {
            takePhoto.onPickFromCaptureWithCrop(fileUri, getCropOptions());
        } else {
            takePhoto.onPickFromCapture(fileUri);
        }
    }

    private void photo() {
        if (enableCrap) {
            takePhoto.onPickMultipleWithCrop(1, getCropOptions());
        } else {
            takePhoto.onPickMultiple(1);
        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhoto.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void takeSuccess(TResult result) {
        String url = result.getImage().getOriginalPath();
        Logger.i("得到的图片地址：%s",url);
        if(tackPhotoResult!=null){
            tackPhotoResult.takeSuccess(url);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        ToastUtils.showToast(context,msg);
    }


    @Override
    public void takeCancel() {

    }

    @Override
    public Intent getPickMultipleIntent(TContextWrap contextWrap, int limit) {
        Intent intent = new Intent(context, ImageSelectorActivity.class);
        intent.putExtra(TConstant.IMAGE_MAX, limit);
        return intent;
    }

    public interface TackPhotoResult{
        void takeSuccess(String path);
    }

}
