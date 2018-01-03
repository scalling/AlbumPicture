package com.zm.picture.sample.mvp.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.sample.R;
import com.zm.tool.library.adapter.BaseListAdapter;
import com.zm.tool.library.adapter.BaseRecyclerHolder;

import java.io.File;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 */
public class ImageListAdapter extends BaseListAdapter<LocalMedia> {
    public static final int TYPE_CAMERA = 1;//拍照
    public static final int TYPE_PICTURE = 2;//图片选择
    private ImageListInterface imageListInterface;
    private boolean isMultiple =false;
    private boolean showCamera = true;//是否显示拍照
    public ImageListAdapter(Context context, ImageListInterface imageListInterface) {
        super(context);
        this.imageListInterface = imageListInterface;
    }
    public ImageListAdapter(Context context, ImageListInterface imageListInterface, boolean isMultiple, boolean showCamera) {
        super(context);
        this.imageListInterface = imageListInterface;
        setMultiple(isMultiple);
        setShowCamera(showCamera);
    }
    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }
    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    @Override
    public int getLayoutId() {
         return R.layout.image_selector_picture_adapter;
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mInflater.inflate(getLayoutId(viewType), parent, false);
        return new BaseRecyclerHolder(itemView);
    }


    public int getLayoutId(int viewType) {
        if (viewType == TYPE_CAMERA) {
            return R.layout.image_selector_camera_adapter;
        } else {
            return R.layout.image_selector_picture_adapter;
        }

    }
    @Override
    public void onBindItemHolder(BaseRecyclerHolder holder, final int position) {
        final LocalMedia dto = getDataList().get(position);
        if(getItemViewType(position)==TYPE_PICTURE){
        final ImageView picture = holder.getView(R.id.picture);
        final ImageView check = holder.getView(R.id.check);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.mipmap.ic_placeholder);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).applyDefaultRequestOptions(options).load(new File(dto.getPath()))
                .transition(DrawableTransitionOptions.withCrossFade()).thumbnail(0.5f).into(picture);
        if (!isMultiple) {
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

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListInterface.onItemClick(getDataList(),dto, position,getItemViewType(position));
            }
        });
    }
    public interface ImageListInterface{
        void checkBoxClick(boolean isChecked, LocalMedia image);
        void onItemClick(List<LocalMedia> images, LocalMedia media, int position, int viewType);
        boolean isSelected(LocalMedia image);
    }

}
