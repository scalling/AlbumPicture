package com.zm.selpicture.lib.ui.adapter;

/**
 * Created by scala on 2018/1/2.
 */

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder基类
 */
public class BaseRecyclerHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;

    public BaseRecyclerHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
