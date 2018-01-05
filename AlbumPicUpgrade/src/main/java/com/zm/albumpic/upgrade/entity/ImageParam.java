package com.zm.albumpic.upgrade.entity;

import java.io.Serializable;

/**
 * Created by scala on 2018/1/3.
 */

public class ImageParam implements Serializable {
    private int maxSelectNum = 0;//默认0单选 否则多选
    private boolean isOri = false;//是否压缩 默认不压缩
    private boolean enablePreview = false;//详情是否可用 默认不可用
    private boolean showCamera = true;//是否显示拍照
    private boolean multiple=false;//
    private boolean enableCrap;//是否允许裁剪
    private int outx = 500;//默认裁剪大小
    private int outy = 500;//默认裁剪大小\

    public ImageParam() {
    }

    public ImageParam(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public ImageParam(int maxSelectNum, boolean enablePreview) {
        setMaxSelectNum(maxSelectNum);
        setEnablePreview(enablePreview);
    }

    public ImageParam(int maxSelectNum, boolean enablePreview, boolean showCamera) {
        setMaxSelectNum(maxSelectNum);
        setEnablePreview(enablePreview);
        setShowCamera(showCamera);
    }

    public ImageParam(int maxSelectNum, boolean enablePreview, boolean showCamera, boolean multiple, boolean enableCrap) {
        setMaxSelectNum(maxSelectNum);
        setEnablePreview(enablePreview);
        setShowCamera(showCamera);
        setMultiple(multiple);
        setEnableCrap(enableCrap);
    }

    public ImageParam(int maxSelectNum, boolean isOri, boolean enablePreview, boolean showCamera, boolean multiple, boolean enableCrap) {
        setMaxSelectNum(maxSelectNum);
        setOri(isOri);
        setEnablePreview(enablePreview);
        setShowCamera(showCamera);
        setMultiple(multiple);
        setEnableCrap(enableCrap);
    }

    public ImageParam(int maxSelectNum, boolean enablePreview, boolean showCamera, boolean multiple, boolean enableCrap, int outx, int outy) {
        setMaxSelectNum(maxSelectNum);
        setEnablePreview(enablePreview);
        setShowCamera(showCamera);
        setMultiple(multiple);
        setEnableCrap(enableCrap);
        setOutx(outx);
        setOuty(outy);
    }

    public ImageParam(int maxSelectNum, boolean isOri, boolean enablePreview, boolean showCamera, boolean multiple, boolean enableCrap, int outx, int outy) {
        setMaxSelectNum(maxSelectNum);
        setOri(isOri);
        setEnablePreview(enablePreview);
        setShowCamera(showCamera);
        setMultiple(multiple);
        setEnableCrap(enableCrap);
        setOutx(outx);
        setOuty(outy);
    }

    public int getOutx() {
        return outx;
    }

    public void setOutx(int outx) {
        this.outx = outx;
    }

    public int getOuty() {
        return outy;
    }

    public void setOuty(int outy) {
        this.outy = outy;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isEnableCrap() {
        return enableCrap;
    }

    public void setEnableCrap(boolean enableCrap) {
        this.enableCrap = enableCrap;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
        if(this.maxSelectNum>1)
            multiple=true;
        else
            multiple=false;
    }

    public boolean isEnablePreview() {
        if (!isMultiple()) {
            enablePreview = false;
        }
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

    public boolean isMultiple() {
        return multiple;
    }
}
