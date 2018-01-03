package com.zm.selpicture.lib.entity;

import com.zm.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scala on 2018/1/3.
 */

public class PreviewParam implements Serializable{
    private int maxSelectNum = 0;//默认0单选 否则多选
    private boolean isOri = false;//是否压缩 默认不压缩
    private int position=0;//点击的图片位置
    private List<LocalMedia> images = new ArrayList<>();//当前文件夹所有的图片路径
    private List<LocalMedia> selectImages = new ArrayList<>();//当前选中的图片路径

    public PreviewParam() {
    }

    public PreviewParam(int maxSelectNum, boolean isOri, int position, List<LocalMedia> images, List<LocalMedia> selectImages) {
        this.maxSelectNum = maxSelectNum;
        this.isOri = isOri;
        this.position = position;
        this.images = images;
        this.selectImages = selectImages;
    }

    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public boolean isOri() {
        return isOri;
    }

    public void setOri(boolean ori) {
        isOri = ori;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<LocalMedia> getImages() {
        return images;
    }

    public void setImages(List<LocalMedia> images) {
        this.images = images;
    }

    public List<LocalMedia> getSelectImages() {
        return selectImages;
    }

    public void setSelectImages(List<LocalMedia> selectImages) {
        this.selectImages = selectImages;
    }
}
