package com.zm.picture.sample.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zm.picture.sample.R;
import com.zm.picture.sample.mvp.ui.activity.ImagePreviewActivity;

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
                ImagePreviewActivity activity = (ImagePreviewActivity) getActivity();
                activity.switchBarVisibility();
            }
        });
        return contentView;
    }
}
