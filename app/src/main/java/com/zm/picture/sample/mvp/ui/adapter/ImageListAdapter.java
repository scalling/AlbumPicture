package com.zm.picture.sample.mvp.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;

import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.sample.R;
import com.zm.picture.sample.mvp.presenter.ImagePresenter;
import com.zm.tool.library.adapter.BaseListAdapter;
import com.zm.tool.library.adapter.BaseRecyclerHolder;
import com.zm.tool.library.widget.GlideImageLoader;

import java.io.File;

/**
 * Created by dee on 15/11/19.
 */
public class ImageListAdapter extends BaseListAdapter<LocalMedia> {
    private ImagePresenter imagePresenter;
    public ImageListAdapter(Context context, ImagePresenter imagePresenter) {
        super(context);
        this.imagePresenter = imagePresenter;
    }
    @Override
    public int getLayoutId() {
        return R.layout.image_selector_picture_adapter;
    }

    @Override
    public void onBindItemHolder(BaseRecyclerHolder holder, final int position) {
        final LocalMedia dto = getDataList().get(position);
        final ImageView picture = holder.getView(R.id.picture);
        final ImageView check = holder.getView(R.id.check);
        GlideImageLoader.create(picture).loadThumbnail(new File(dto.getPath()),  R.drawable.image_placeholder,0.5f);
        if (!imagePresenter.isMultiple()) {
            check.setVisibility(View.GONE);
        }
        boolean isChecked =imagePresenter.isSelected(dto);
        check.setSelected(isChecked);
        if (isChecked) {
            picture.setColorFilter(mContext.getResources().getColor(R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            picture.setColorFilter(mContext.getResources().getColor(R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePresenter.checkBoxClick(check.isSelected(), dto);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePresenter.onItemClick(getDataList(),dto, position,check.isSelected());
            }
        });
    }

}
