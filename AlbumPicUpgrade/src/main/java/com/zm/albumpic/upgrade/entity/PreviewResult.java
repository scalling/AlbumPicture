package com.zm.albumpic.upgrade.entity;

import com.zm.albumpic.entity.LocalMedia;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scala on 2018/1/3.
 */

public class PreviewResult implements Serializable{
    private boolean isDone;//是否完成
    private boolean isOri = false;//是否压缩 默认不压缩
    private List<LocalMedia> images;//选中的图片

    public PreviewResult() {
    }

    public PreviewResult(boolean isDone, boolean isOri, List<LocalMedia> images) {
        this.isDone = isDone;
        this.isOri = isOri;
        this.images = images;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isOri() {
        return isOri;
    }

    public void setOri(boolean ori) {
        isOri = ori;
    }

    public List<LocalMedia> getImages() {
        return images;
    }

    public void setImages(List<LocalMedia> images) {
        this.images = images;
    }
}
