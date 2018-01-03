package com.zm.selpicture.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zm.picture.lib.util.TConstant;
import com.zm.selpicture.lib.entity.ImageParam;
import com.zm.selpicture.lib.presenter.ImagePresenter;
import com.zm.selpicture.lib.ui.popup.TackPhotoPopup;

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
            tackPhotoPopup = new TackPhotoPopup(this, new TackPhotoPopup.TackPhotoResult() {
                @Override
                public void takeSuccess(String path) {
                    Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT);
                }
            });
        isM = false;
        tackPhotoPopup.show();
    }

    @OnClick(R.id.btn_m)
    void btnM() {
        isM = true;
        ImagePresenter.open(this, ImageSelectorActivity.class, new ImageParam(9, true), TConstant.RC_PICK_MULTIPLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!isM) {
            tackPhotoPopup.onActivityResult(requestCode, resultCode, data);
        } else {
            if (requestCode == com.zm.picture.lib.util.TConstant.RC_PICK_MULTIPLE && resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(TConstant.REQUEST_OUTPUT);
                Toast.makeText(MainActivity.this, images.toString(), Toast.LENGTH_SHORT);
            }

        }

    }
}
