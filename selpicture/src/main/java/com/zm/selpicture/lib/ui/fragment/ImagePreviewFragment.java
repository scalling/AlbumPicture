package com.zm.selpicture.lib.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zm.selpicture.lib.R;

import org.simple.eventbus.EventBus;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by dee on 15/11/25.
 */
public class ImagePreviewFragment extends Fragment {
    public static final String PATH = "path";

    public static ImagePreviewFragment getInstance(String path) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.image_preview_fragment, container, false);
        final PhotoView imageView = (PhotoView) contentView.findViewById(R.id.preview_image);
        Glide.with(container.getContext())
                .asBitmap()
                .load(new File(getArguments().getString(PATH)))
                .into(imageView);
        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                EventBus.getDefault().post(new Intent(),"onViewTap");
//                ImagePreviewActivity activity = (ImagePreviewActivity) getActivity();
//                activity.switchBarVisibility();
            }
        });
        return contentView;
    }


}
