package com.zm.selpicture.lib.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zm.picture.lib.entity.LocalMedia;
import com.zm.selpicture.lib.ui.fragment.ImagePreviewFragment;

import java.util.List;

/**
 * 内容:图片详情item
 * 日期:2018/1/1
 * 创建人:scala
 */

public class ImagePreviewPagerAdapter extends FragmentPagerAdapter {
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
