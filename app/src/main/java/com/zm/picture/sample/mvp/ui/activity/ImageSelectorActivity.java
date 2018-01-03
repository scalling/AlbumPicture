package com.zm.picture.sample.mvp.ui.activity;

/**
 * Created by shake on 2017/8/31.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.sample.R;
import com.zm.picture.sample.base.BaseMVPAppCompatActivity;
import com.zm.picture.sample.mvp.contract.ImageContract;
import com.zm.picture.sample.mvp.model.entity.PreviewParam;
import com.zm.picture.sample.mvp.presenter.ImagePresenter;
import com.zm.picture.sample.mvp.presenter.PreviewPresenter;
import com.zm.picture.sample.mvp.ui.adapter.ImageFolderAdapter;
import com.zm.picture.sample.mvp.ui.adapter.ImageListAdapter;
import com.zm.picture.sample.mvp.ui.popup.FolderPopup;
import com.zm.picture.sample.mvp.ui.widget.GridSpacingItemDecoration;
import com.zm.tool.library.util.ScreenUtils;
import com.zm.tool.library.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shake on 2017/8/30.
 */

public class ImageSelectorActivity extends BaseMVPAppCompatActivity<ImageContract.IView, ImagePresenter> implements ImageContract.IView {
    @BindView(R.id.tvRests)
    TextView tvRests;
    @BindView(R.id.preview_text)
    TextView previewText;
    @BindView(R.id.folder_list)
    RecyclerView recyclerView;
    @BindView(R.id.folder_layout)
    LinearLayout folderLayout;
    @BindView(R.id.folder_name)
    TextView folderName;
    @BindView(R.id.mTitle)
    TextView tvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    private FolderPopup folderWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector_activity);
        ButterKnife.bind(this);
        tvTitle.setText(getString(R.string.picture));
        getPresenter().loadData(this);
    }
    @Override
    public void bindAdapter(ImageListAdapter adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(this, 2), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btnBack, R.id.folder_layout, R.id.tvRests, R.id.preview_text})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.folder_layout) {
            showFolder();
        } else if (id == R.id.tvRests) {
            getPresenter().onDoneClick();
        } else if (id == R.id.preview_text) {
            getPresenter().startSelPreview();
        }
    }

    @Override
    public void bindFolder(List<LocalMediaFolder> folders) {
        getFolderPopup().bindFolder(folders);
    }

    @Override
    public void setFolderName(String name) {
        folderName.setText(name);
    }


    @Override
    public void onFinish() {
        finish();
    }

    @NonNull
    @Override
    public ImagePresenter createPresenter() {
        return new ImagePresenter();
    }

    public void showFolder() {
        if (getFolderPopup().isShowing()) {
            getFolderPopup().dismiss();
        } else {
            getFolderPopup().showAsDropDown(rlTitle);
        }
    }
    private FolderPopup getFolderPopup() {
        if (folderWindow == null) {
            folderWindow = new FolderPopup(this);
            folderWindow.setOnItemClickListener(new ImageFolderAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String name, List<LocalMedia> images) {
                    folderWindow.dismiss();
                    getPresenter().selectFolderImages(name, images);

                }
            });
        }
        return folderWindow;
    }

    @Override
    public void onMaxError(int maxSize) {
        ToastUtils.showToast(this, getString(R.string.message_max_num, maxSize + ""));
    }

    @Override
    public void setPreviewVisibility(boolean flag) {
        previewText.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRestVisibility(boolean flag) {
        tvRests.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTvRestText(int size, int maxSelectNum) {
        if(size<=0)
            tvRests.setText(getString(R.string.done));
        else
            tvRests.setText(getString(R.string.done_num, size + "", maxSelectNum + ""));
    }

    @Override
    public void setPreviewText(int size) {
        if(size<=0)
            previewText.setText(getString(R.string.preview));
        else
            previewText.setText(getString(R.string.preview_num, size + ""));

    }

    @Override
    public void setPrevieParam(PreviewParam previeParam) {
        PreviewPresenter.open(this,ImagePreviewActivity.class,previeParam,0);
    }



    @Override
    public void setSelEnable(boolean enable) {
        tvRests.setEnabled(enable);
        previewText.setEnabled(enable);
    }
}
