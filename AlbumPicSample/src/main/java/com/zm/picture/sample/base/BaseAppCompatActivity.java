package com.zm.picture.sample.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zm.picture.sample.AppManager;


/**
 * Created by shake on 2017/8/29.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this.getClass().getName(), this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
