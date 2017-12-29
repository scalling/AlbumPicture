package com.zm.picture.sample.mvp.model;


import com.zm.picture.sample.mvp.contract.PreviewContract;

/**
 * Created by shake on 2017/9/1.
 */

public class PreviewModel implements PreviewContract.IModel {
    @Override
    public String getFileSize(long size) {
        StringBuilder strSize = new StringBuilder();
        if (size < 1024) {
            strSize.append(size).append("B");
        } else if (size < 1024 * 1024) {
            strSize.append(size / 1024).append("K");
        } else {
            strSize.append(size / 1024 / 1024).append("M");
        }
        return strSize.toString();
    }
}
