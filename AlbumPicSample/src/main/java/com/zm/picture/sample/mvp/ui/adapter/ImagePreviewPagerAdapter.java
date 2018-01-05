package com.zm.picture.sample.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.zm.albumpic.entity.LocalMedia;
import com.zm.picture.sample.mvp.ui.fragment.ImagePreviewFragment;

import java.util.List;

/**
 * Created by shake on 2017/8/31.
 */

public class ImagePreviewPagerAdapter  extends FragmentPagerAdapter {
    private List<LocalMedia> dtos;
    public ImagePreviewPagerAdapter(FragmentManager fm, List<LocalMedia> dtos) {
        super(fm);
        this.dtos = dtos;
    }
    @Override
    public Fragment getItem(int position) {
        return ImagePreviewFragment.getInstance(dtos.get(position).getPath());
    }

    @Override
    public int getCount() {
        return dtos.size();
    }
}
