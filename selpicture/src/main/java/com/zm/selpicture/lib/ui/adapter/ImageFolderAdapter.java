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
 * Created by dee on 15/11/20.
 */
public class ImageFolderAdapter extends BaseListAdapter<LocalMediaFolder> {
    private int checkedIndex = 0;

    private OnItemClickListener onItemClickListener;
    public ImageFolderAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.folder_popup_adapter;
    }
    @Override
    public void onBindItemHolder(BaseRecyclerHolder holder, final int position) {
        ImageView firstImage =holder.getView(R.id.first_image);
        TextView folderName = holder.getView(R.id.folder_name);
        TextView imageNum = holder.getView(R.id.image_num);
        ImageView isSelected = holder.getView(R.id.is_selected);

        final LocalMediaFolder folder = getDataList().get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_placeholder);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).applyDefaultRequestOptions(options).load(new File(folder.getFirstImagePath()))
                .transition(DrawableTransitionOptions.withCrossFade()).thumbnail(0.5f).into(firstImage);
       folderName.setText(folder.getName());
       imageNum.setText(mContext.getString(R.string.num_postfix,folder.getImageNum()+""));

       isSelected.setVisibility(checkedIndex == position ? View.VISIBLE : View.GONE);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    checkedIndex = position;
                    notifyDataSetChanged();
                    onItemClickListener.onItemClick(folder.getName(),folder.getImages());
                }
            }
        });
    }
    public void bindFolder(List<LocalMediaFolder> folders){
        setDataList(folders);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(String folderName, List<LocalMedia> images);
    }
}
