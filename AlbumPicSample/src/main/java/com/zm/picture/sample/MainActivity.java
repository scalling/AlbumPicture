package com.zm.picture.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zm.albumpic.util.TConstant;
import com.zm.picture.sample.mvp.model.entity.ImageParam;
import com.zm.picture.sample.mvp.presenter.ImagePresenter;
import com.zm.picture.sample.mvp.ui.activity.ImageSelectorActivity;
import com.zm.picture.sample.mvp.ui.popup.TackPhotoPopup;
import com.zm.tool.library.util.ToastUtils;
import com.zm.tool.library.widget.GlideImageLoader;
import com.zm.tool.library.widget.RoundedImageView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.iv_head)
    RoundedImageView iv;
    private TackPhotoPopup tackPhotoPopup;
    boolean isM = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_tack)
    void click() {
        if (tackPhotoPopup == null)
            tackPhotoPopup = new TackPhotoPopup(this, new TackPhotoPopup.TackPhotoResult() {
                @Override
                public void takeSuccess(String path) {
                    GlideImageLoader.create(iv).load(new File(path), R.mipmap.ic_placeholder);
                }
            });
        isM = false;
        tackPhotoPopup.show();
    }

    @OnClick(R.id.btn_m)
    void btnM(){
        isM = true;
        ImagePresenter.open(this,ImageSelectorActivity.class,new ImageParam(9,true), TConstant.RC_PICK_MULTIPLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(!isM){
            tackPhotoPopup.onActivityResult(requestCode, resultCode, data);
        }else{
           if(requestCode==TConstant.RC_PICK_MULTIPLE&&resultCode == Activity.RESULT_OK && data != null){
               ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(TConstant.REQUEST_OUTPUT);
               ToastUtils.showToast(MainActivity.this,images.toString());
           }

        }

    }
}
