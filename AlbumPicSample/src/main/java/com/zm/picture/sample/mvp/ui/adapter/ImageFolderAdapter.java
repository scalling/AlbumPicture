package com.zm.picture.sample.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zm.albumpic.entity.LocalMedia;
import com.zm.albumpic.entity.LocalMediaFolder;
import com.zm.picture.sample.R;
import com.zm.tool.library.adapter.BaseListAdapter;
import com.zm.tool.library.adapter.BaseRecyclerHolder;
import com.zm.tool.library.widget.GlideImageLoader;

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
        GlideImageLoader.create(firstImage).load(new File(folder.getFirstImagePath()),R.mipmap.ic_placeholder);
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
