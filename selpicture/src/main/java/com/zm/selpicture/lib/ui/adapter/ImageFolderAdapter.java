package com.zm.selpicture.lib.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.selpicture.lib.R;

import java.io.File;
import java.util.List;

/**
 * 内容:图片文件夹item
 * 日期:2018/1/1
 * 创建人:scala
 */
public class ImageFolderAdapter extends BaseListAdapter<LocalMediaFolder> {
    private int checkedIndex = 0;
    private Builder builder;
    private OnItemClickListener onItemClickListener;

    private ImageFolderAdapter(Context context, Builder builder) {
        super(context);
        this.builder = builder;
    }

    public void setCheckedIndex(int checkedIndex) {
        this.checkedIndex = checkedIndex;
    }

    @Override
    public int getLayoutId() {
        return builder.getLayoutId();
    }

    @Override
    public void onBindItemHolder(BaseRecyclerHolder holder, final int position) {
        ImageView ivFolderPic = holder.getView(builder.getIvFolderPicId());
        TextView tvFolderName = holder.getView(builder.getTvFolderNameId());
        TextView tvFolderNum = holder.getView(builder.getTvFolderNumId());
        ImageView ivChecked = holder.getView(builder.getIvCheckedId());

        final LocalMediaFolder folder = getDataList().get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(builder.getImageResource());
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).applyDefaultRequestOptions(options).load(new File(folder.getFirstImagePath()))
                .transition(DrawableTransitionOptions.withCrossFade()).thumbnail(0.5f).into(ivFolderPic);
        tvFolderName.setText(folder.getName());
        tvFolderNum.setText(mContext.getString(R.string.num_postfix, folder.getImageNum() + ""));
        ivChecked.setVisibility(checkedIndex == position ? View.VISIBLE : View.GONE);
        ivChecked.setImageResource(builder.getSelectedImageResource());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                checkedIndex = position;
                notifyDataSetChanged();
                onItemClickListener.onItemClick(folder.getName(), folder.getImages());
            }
        });
    }

    public void bindFolder(List<LocalMediaFolder> folders) {
        setDataList(folders);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if(this.onItemClickListener==null)
            this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String folderName, List<LocalMedia> images);
    }


    /**
     * 配置文件
     */
    public static final class Builder {

        int layoutId = R.layout.folder_popup_adapter;//布局文件
        int imageResource = R.drawable.ic_placeholder;//图片默认图片
        int selectedImageResource = R.drawable.folder_radio;//选中的图片

        //view
        int ivFolderPicId = R.id.first_image;//文件夹的图片id 必须是imageView
        int tvFolderNameId = R.id.folder_name;//文件夹的名称Id 必须是TextView
        int tvFolderNumId = R.id.image_num;//文件夹内文件的数量id 必须是TextView
        int ivCheckedId = R.id.is_selected;//单选框 必须是imageView

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setLayoutId(int layoutId, int ivFolderPicId, int tvFolderNameId, int tvFolderNumId, int ivCheckedId) {
            this.layoutId = layoutId;
            this.ivFolderPicId = ivFolderPicId;
            this.tvFolderNameId = tvFolderNameId;
            this.tvFolderNumId = tvFolderNumId;
            this.ivCheckedId = ivCheckedId;
            return this;
        }

        public Builder setImageResource(int imageResource) {
            this.imageResource = imageResource;
            return this;
        }

        public Builder setSelectedImageResource(int selectedImageResource) {
            this.selectedImageResource = selectedImageResource;
            return this;
        }

        public int getIvFolderPicId() {
            return ivFolderPicId;
        }

        public int getTvFolderNameId() {
            return tvFolderNameId;
        }

        public int getTvFolderNumId() {
            return tvFolderNumId;
        }

        public int getIvCheckedId() {
            return ivCheckedId;
        }

        public int getLayoutId() {
            return layoutId;
        }

        public int getImageResource() {
            return imageResource;
        }

        public int getSelectedImageResource() {
            return selectedImageResource;
        }

        public ImageFolderAdapter build(Context context) {
            return new ImageFolderAdapter(context, this);
        }

    }
}
