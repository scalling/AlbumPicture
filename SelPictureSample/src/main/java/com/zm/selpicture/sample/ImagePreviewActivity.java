package com.zm.selpicture.sample;

/**
 * Created by shake on 2017/8/31.
 */

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zm.selpicture.lib.contract.PreviewContract;
import com.zm.selpicture.lib.presenter.PreviewPresenter;
import com.zm.selpicture.lib.ui.adapter.ImagePreviewPagerAdapter;
import com.zm.selpicture.lib.ui.widget.PreviewViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by shake on 2017/8/30.
 */
public class ImagePreviewActivity extends AppCompatActivity implements PreviewContract.IView {
    @BindView(R.id.bar_layout)
    LinearLayout barLayout;
    @BindView(R.id.select_bar_layout)
    RelativeLayout selectBarLayout;
    @BindView(R.id.tvRests)
    TextView doneText;
    @BindView(R.id.checkbox_select)
    CheckBox checkboxSelect;
    @BindView(R.id.checkbox_pic)
    CheckBox checkPic;
    @BindView(R.id.preview_pager)
    PreviewViewPager viewPager;
    @BindView(R.id.mTitle)
    TextView tvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    private PreviewPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview_activity);
        ButterKnife.bind(this);
        mPresenter = new PreviewPresenter();
        mPresenter.attachView(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    public void initView() {
        mPresenter.onCreate(this);
    }

    @OnPageChange(R.id.preview_pager)
    public void onPageSelected(int position) {
        mPresenter.onPageSelected(position);
    }

    @OnCheckedChanged(R.id.checkbox_pic)
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mPresenter.setIsOri(b, viewPager.getCurrentItem());
    }

    @Override
    public void setAdapter(ImagePreviewPagerAdapter adapter, int selPosition) {
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selPosition);
    }

    @Override
    public void setOriText(String size) {
        if (!TextUtils.isEmpty(size)) {
            checkPic.setText(getString(R.string.pic_file_num, size + ""));
        } else {
            checkPic.setText(getString(R.string.pic_file));
        }
    }

    @Override
    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void isOri(boolean checked) {
        checkPic.setChecked(checked);
    }

    @OnClick({R.id.btnBack, R.id.checkbox_select, R.id.tvRests})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            mPresenter.onDoneClick(false);
        } else if (id == R.id.checkbox_select) {
            mPresenter.checkClick(checkboxSelect.isChecked(), viewPager.getCurrentItem());
        } else if (id == R.id.tvRests) {
            mPresenter.onDoneClick(true);
        }
    }

    @Override
    public void setDoneText(int size, int maxSelectNum) {
        boolean enable = size != 0;
        doneText.setEnabled(enable);
        if (enable) {
            doneText.setText(getString(R.string.done_num, size + "", maxSelectNum + ""));
        } else {
            doneText.setText(R.string.done);
        }
    }

    @Override
    public void setSelect(boolean checked) {
        checkboxSelect.setChecked(checked);
    }

    @Override
    public void hideStatusBar() {
        barLayout.setVisibility(View.GONE);
        rlTitle.setVisibility(View.GONE);
        selectBarLayout.setVisibility(View.GONE);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    @Override
    public void showStatusBar() {
        barLayout.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
        selectBarLayout.setVisibility(View.VISIBLE);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    @Override
    public void selMaxError(int maxSelectNum) {
        Toast.makeText(this, getString(R.string.message_max_num, maxSelectNum + ""), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        finish();
    }

}
