package com.zm.selpicture.lib.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zm.picture.lib.entity.LocalMedia;
import com.zm.selpicture.lib.R;

import java.io.File;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 */
public class ImageListAdapter extends BaseListAdapter<LocalMedia> {
    private ImageListInterface imageListInterface;
    public ImageListAdapter(Context context, ImageListInterface imageListInterface) {
        super(context);
        this.imageListInterface = imageListInterface;
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

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_placeholder);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).applyDefaultRequestOptions(options).load(new File(dto.getPath()))
                .transition(DrawableTransitionOptions.withCrossFade()).thumbnail(0.5f).into(picture);
        if (!imageListInterface.isMultiple()) {
            check.setVisibility(View.GONE);
        }
        boolean isChecked =imageListInterface.isSelected(dto);
        check.setSelected(isChecked);
        if (isChecked) {
            picture.setColorFilter(mContext.getResources().getColor(R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            picture.setColorFilter(mContext.getResources().getColor(R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListInterface.checkBoxClick(check.isSelected(), dto);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListInterface.onItemClick(getDataList(),dto, position,check.isSelected());
            }
        });
    }
    public interface ImageListInterface{
        void checkBoxClick(boolean isChecked, LocalMedia image);
        void onItemClick(List<LocalMedia> images, LocalMedia media, int position, boolean isChecked);
        boolean isSelected(LocalMedia image);
        boolean isMultiple();
    }

}
