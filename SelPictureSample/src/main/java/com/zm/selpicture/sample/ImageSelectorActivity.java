package com.zm.selpicture.sample;

/**
 * Created by shake on 2017/8/31.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zm.picture.lib.TakePhoto;
import com.zm.picture.lib.TakePhotoImpl;
import com.zm.picture.lib.entity.LocalMedia;
import com.zm.picture.lib.entity.LocalMediaFolder;
import com.zm.picture.lib.entity.TResult;
import com.zm.selpicture.lib.contract.ImageContract;
import com.zm.selpicture.lib.entity.PreviewParam;
import com.zm.selpicture.lib.presenter.ImagePresenter;
import com.zm.selpicture.lib.presenter.PreviewPresenter;
import com.zm.selpicture.lib.ui.adapter.ImageListAdapter;
import com.zm.selpicture.lib.ui.popup.FolderPopup;
import com.zm.selpicture.lib.ui.widget.GridSpacingItemDecoration;
import com.zm.selpicture.lib.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shake on 2017/8/30.
 */

public class ImageSelectorActivity extends AppCompatActivity implements ImageContract.IView {
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
    private ImagePresenter mPresenter;
    private ImageListAdapter adapter;
    private FolderPopup folderPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector_activity);
        ButterKnife.bind(this);
        adapter = new ImageListAdapter.Builder().build(this);
        mPresenter = new ImagePresenter(adapter);
        mPresenter.attachView(this);

        initView();
    }

    @Override
    public void setData(List<LocalMedia> datas) {
        adapter.setDataList(datas);
    }

    @Override
    public void setFolder(List<LocalMediaFolder> folders) {
        getFolderPopup().bindFolder(folders);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    private void initView() {
        tvTitle.setText(getString(R.string.picture));
        mPresenter.initParams(this);
        mPresenter.loadData(this); //授权获取数据
    }

    @Override
    public void bindAdapter(RecyclerView.Adapter adapter) {
        this.adapter.setImageListInterface(mPresenter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, ScreenUtils.dip2px(this, 2), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btnBack, R.id.folder_layout, R.id.tvRests, R.id.preview_text})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.folder_layout) {
            mPresenter.showFolderPopup(rlTitle);
        } else if (id == R.id.tvRests) {
            mPresenter.onDoneClick();
        } else if (id == R.id.preview_text) {
            mPresenter.startSelPreview();
        }
    }

    @Override
    public void setFolderName(String name) {
        folderName.setText(name);
    }

    @Override
    public void openCamera() {
        //授权点击拍照
        mPresenter.clickCamera();
    }

    @Override
    public void onFinish() {
        finish();
    }

    @Override
    public FolderPopup getFolderPopup() {
        if(folderPopup==null)
            folderPopup = new FolderPopup(this);
        return folderPopup;
    }

    @Override
    public void onMaxError(int maxSize) {
        Toast.makeText(this, getString(R.string.message_max_num, maxSize + ""), Toast.LENGTH_SHORT);
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
    public void checkSel(boolean doneEnable, int selSize, int maxSize) {
        tvRests.setEnabled(doneEnable);
        previewText.setEnabled(doneEnable);
        if (selSize <= 0) {
            tvRests.setText(getString(R.string.done));
            previewText.setText(getString(R.string.preview));
        } else {
            tvRests.setText(getString(R.string.done_num, selSize + "", maxSize + ""));
            previewText.setText(getString(R.string.preview_num, selSize + ""));
        }
    }
    @Override
    public void setPrevieParam(PreviewParam previeParam) {
        PreviewPresenter.open(this, ImagePreviewActivity.class, previeParam, 0);
    }

    @Override
    public TakePhoto getTakePhoto(TakePhoto.TakeResultListener listener) {
        return new TakePhotoImpl(this, listener);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Toast.makeText(this, "裁剪失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        Toast.makeText(this, "裁剪取消", Toast.LENGTH_SHORT).show();
    }
}
