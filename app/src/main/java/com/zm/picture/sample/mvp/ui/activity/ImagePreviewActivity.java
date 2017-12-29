package com.zm.picture.sample.mvp.ui.activity;

/**
 * Created by shake on 2017/8/31.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zm.picture.sample.R;
import com.zm.picture.sample.base.BaseMVPAppCompatActivity;
import com.zm.picture.sample.mvp.contract.PreviewContract;
import com.zm.picture.sample.mvp.presenter.PreviewPresenter;
import com.zm.picture.sample.mvp.ui.adapter.ImagePreviewPagerAdapter;
import com.zm.picture.sample.mvp.ui.widget.PreviewViewPager;
import com.zm.tool.library.util.StringUtils;
import com.zm.tool.library.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shake on 2017/8/30.
 */
public class ImagePreviewActivity extends BaseMVPAppCompatActivity<PreviewContract.IView, PreviewPresenter> implements PreviewContract.IView {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview_activity);
        ButterKnife.bind(this);
        initView();
    }
    public void initView() {
        getPresenter().onCreate();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getPresenter().onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        checkPic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getPresenter().setIsOri(b, viewPager.getCurrentItem());
            }
        });

    }

    @Override
    public void setAdapter(ImagePreviewPagerAdapter adapter, int selPosition) {
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selPosition);
    }

    @Override
    public void setOriText(String str) {
        checkPic.setText(str);
    }


    @NonNull
    @Override
    public PreviewPresenter createPresenter() {
        return new PreviewPresenter();
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
            getPresenter().onDoneClick(false);
        } else if (id == R.id.checkbox_select) {
            getPresenter().checkClick(checkboxSelect.isChecked(), viewPager.getCurrentItem());
        } else if (id == R.id.tvRests) {
            getPresenter().onDoneClick(true);
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
        ToastUtils.showToast(this, getString(R.string.message_max_num, maxSelectNum + ""));
    }

    @Override
    public void onFinish() {
        finish();
    }

    public void switchBarVisibility() {
        getPresenter().switchBarVisibility();
    }

    @Override
    public String getOriSizeText(String size) {
        if (StringUtils.isNotEmpty(size)) {
            return getString(R.string.pic_file_num, size);
        } else {
            return getString(R.string.pic_file);
        }
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }
}
