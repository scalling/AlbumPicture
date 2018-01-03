package com.zm.picture.sample.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by scala on 2018/1/3.
 */

public class ImageParam implements Serializable {
    private int maxSelectNum = 0;//默认0单选 否则多选
    private boolean isOri = false;//是否压缩 默认不压缩
    private boolean enablePreview = false;//详情是否可用 默认不可用

    public ImageParam() {
    }

    public ImageParam(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public ImageParam(int maxSelectNum, boolean enablePreview) {
        this.maxSelectNum = maxSelectNum;
        this.enablePreview = enablePreview;
    }

    public ImageParam(int maxSelectNum, boolean isOri, boolean enablePreview) {
        this.maxSelectNum = maxSelectNum;
        this.isOri = isOri;
        this.enablePreview = enablePreview;
    }

    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public boolean isEnablePreview() {
        return enablePreview;
    }

    public void setEnablePreview(boolean enablePreview) {
        this.enablePreview = enablePreview;
    }

    public boolean isOri() {
        return isOri;
    }

    public void setOri(boolean ori) {
        isOri = ori;
    }
}
