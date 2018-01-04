package com.zm.selpicture.lib.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
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
    public static final int TYPE_CAMERA = 1;//拍照
    public static final int TYPE_PICTURE = 2;//图片选择
    private ImageListInterface imageListInterface;
    private boolean isMultiple = false;
    private boolean showCamera = true;//是否显示拍照
    private Builder builder;

    private ImageListAdapter(Context context, Builder builder) {
        super(context);
        this.builder = builder;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    public void setImageListInterface(ImageListInterface imageListInterface) {
        this.imageListInterface = imageListInterface;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(getLayoutId(viewType), parent, false);
        return new BaseRecyclerHolder(itemView);
    }

    public int getLayoutId(int viewType) {
        if (viewType == TYPE_CAMERA) {
            return builder.getCameraLayoutId();
        } else {
            return builder.getPicLayoutId();
        }

    }

    @Override
    public void onBindItemHolder(BaseRecyclerHolder holder, final int position) {
        final LocalMedia dto = getDataList().get(position);
        if (getItemViewType(position) == TYPE_PICTURE) {
            final ImageView picture = holder.getView(builder.getPicIvId());
            final ImageView check = holder.getView(builder.getCheckIvId());
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(builder.getPicImageResource());
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext).applyDefaultRequestOptions(options).load(new File(dto.getPath()))
                    .transition(DrawableTransitionOptions.withCrossFade()).thumbnail(0.5f).into(picture);
            if (!isMultiple) {
                check.setVisibility(View.GONE);
            }
            boolean isChecked = imageListInterface.isSelected(dto);
            check.setSelected(isChecked);
            check.setImageResource(builder.getCheckIvImageResource());
            if (isChecked) {
                picture.setColorFilter(mContext.getResources().getColor(builder.getPicCheckedColor()), PorterDuff.Mode.SRC_ATOP);
            } else {
                picture.setColorFilter(mContext.getResources().getColor(builder.getPicNoCheckedColor()), PorterDuff.Mode.SRC_ATOP);
            }
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageListInterface.checkBoxClick(check.isSelected(), dto);
                }
            });
        } else {
            final ImageView ivCamera = holder.getView(builder.getCameraIvId());
            ivCamera.setImageResource(builder.getCameraImageResource());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListInterface.onItemClick(getDataList(), dto, position, getItemViewType(position));
            }
        });
    }

    public interface ImageListInterface {
        void checkBoxClick(boolean isChecked, LocalMedia image);

        void onItemClick(List<LocalMedia> images, LocalMedia media, int position, int viewType);

        boolean isSelected(LocalMedia image);
    }


    /**
     * 配置文件
     */
    public static final class Builder {

        //相机
        int cameraLayoutId = R.layout.image_selector_camera_adapter;//拍照item
        int cameraIvId = R.id.iv_camera;//拍照控件ID 必须是ImageView

        int cameraImageResource = R.drawable.ic_camera;//拍照背景

        //图片
        int picLayoutId = R.layout.image_selector_picture_adapter;//图片item
        int picIvId = R.id.picture;//图片控件ID 必须是ImageView

        int picImageResource = R.drawable.ic_placeholder;//图片默认图片
        int picCheckedColor = R.color.image_overlay2;//选中的背景
        int picNoCheckedColor = R.color.image_overlay;//未选中的背景

        //复选框
        int checkIvId = R.id.check;//图片复选框ID 必须是ImageView

        int checkIvImageResource = R.drawable.image_checkbox_selector;

        public Builder setCameraLayoutId(int cameraLayoutId) {
            this.cameraLayoutId = cameraLayoutId;
            return this;
        }

        public Builder setPicLayoutId(int picLayoutId) {
            this.picLayoutId = picLayoutId;
            return this;
        }

        public Builder setCameraLayoutId(int cameraLayoutId, int cameraIvId) {
            this.cameraLayoutId = cameraLayoutId;
            this.cameraIvId = cameraIvId;
            return this;
        }

        public Builder setPicLayoutId(int picLayoutId, int picIvId, int checkIvId) {
            this.picLayoutId = picLayoutId;
            this.picIvId = picIvId;
            this.checkIvId = checkIvId;
            return this;
        }

        public Builder setCameraImageResource(int cameraImageResource) {
            this.cameraImageResource = cameraImageResource;
            return this;
        }

        public Builder setPicImageResource(int picImageResource) {
            this.picImageResource = picImageResource;
            return this;
        }

        public Builder setPicCheckedColor(int picCheckedColor) {
            this.picCheckedColor = picCheckedColor;
            return this;
        }

        public Builder setPicNoCheckedColor(int picNoCheckedColor) {
            this.picNoCheckedColor = picNoCheckedColor;
            return this;
        }

        public Builder setCheckIvImageResource(int checkIvImageResource) {
            this.checkIvImageResource = checkIvImageResource;
            return this;
        }

        public int getCameraLayoutId() {
            return cameraLayoutId;
        }

        public int getCameraIvId() {
            return cameraIvId;
        }

        public int getCameraImageResource() {
            return cameraImageResource;
        }

        public int getPicLayoutId() {
            return picLayoutId;
        }

        public int getPicIvId() {
            return picIvId;
        }

        public int getPicImageResource() {
            return picImageResource;
        }

        public int getPicCheckedColor() {
            return picCheckedColor;
        }

        public int getPicNoCheckedColor() {
            return picNoCheckedColor;
        }

        public int getCheckIvId() {
            return checkIvId;
        }

        public int getCheckIvImageResource() {
            return checkIvImageResource;
        }

        public ImageListAdapter build(Context context) {
            return new ImageListAdapter(context, this);
        }
    }

}
