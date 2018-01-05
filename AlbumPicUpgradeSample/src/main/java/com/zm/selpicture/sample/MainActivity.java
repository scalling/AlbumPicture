package com.zm.selpicture.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zm.albumpic.TakePhoto;
import com.zm.albumpic.entity.TContextWrap;
import com.zm.albumpic.entity.TResult;
import com.zm.albumpic.upgrade.entity.ImageParam;
import com.zm.albumpic.upgrade.presenter.ImagePresenter;
import com.zm.albumpic.upgrade.ui.popup.TackPhotoPopup;
import com.zm.albumpic.util.TConstant;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by scala on 2018/1/2.
 */

public class MainActivity extends AppCompatActivity {
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
            tackPhotoPopup = new TackPhotoPopup.Builder()
                    .setTackPhotoResult(new TakePhoto.TakeResultListener() {
                        @Override
                        public void takeSuccess(TResult result) {
                            String url = result.getImage().getOriginalPath();
                            showMsg(url);
                        }

                        @Override
                        public void takeFail(TResult result, String msg) {
                            showMsg("裁剪失败");
                        }

                        @Override
                        public void takeCancel() {
                            showMsg("裁剪取消");
                        }

                        @Override
                        public Intent getPickMultipleIntent(TContextWrap contextWrap, int limit) {
                            return ImagePresenter.getOpenIntent(contextWrap.getActivity(), ImageSelectorActivity.class, new ImageParam(limit));
                        }
                    }).build(this);
        isM = false;
        tackPhotoPopup.show();
    }

    @OnClick(R.id.btn_m)
    void btnM() {
        isM = true;
        ImagePresenter.open(this, ImageSelectorActivity.class, new ImageParam(2, true, true), TConstant.RC_PICK_MULTIPLE);
    }
    @OnClick(R.id.btn_no_camera)
    void noCamera() {
        isM = true;
        ImagePresenter.open(this, ImageSelectorActivity.class, new ImageParam(2, true, false), TConstant.RC_PICK_MULTIPLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!isM) {
            tackPhotoPopup.onActivityResult(requestCode, resultCode, data);
        } else {
            if (requestCode == TConstant.RC_PICK_MULTIPLE && resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(TConstant.REQUEST_OUTPUT);
                Toast.makeText(MainActivity.this, images.toString(), Toast.LENGTH_SHORT);
            }

        }

    }

    public void showMsg(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
