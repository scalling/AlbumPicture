package com.zm.picture.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
        Intent intent = new Intent(this, ImageSelectorActivity.class);
        intent.putExtra(TConstant.IMAGE_MAX, 9);
        intent.putExtra(TConstant.IMAGE_ENABLE_PREVIEW,true);
        startActivityForResult(intent, com.zm.picture.lib.util.TConstant.RC_PICK_MULTIPLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(!isM){
            tackPhotoPopup.onActivityResult(requestCode, resultCode, data);
        }else{
           if(requestCode==com.zm.picture.lib.util.TConstant.RC_PICK_MULTIPLE&&resultCode == Activity.RESULT_OK && data != null){
               ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(com.zm.picture.lib.util.TConstant.REQUEST_OUTPUT);
               ToastUtils.showToast(MainActivity.this,images.toString());
           }

        }

    }
}
