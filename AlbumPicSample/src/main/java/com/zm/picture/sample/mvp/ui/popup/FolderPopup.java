package com.zm.picture.sample.mvp.ui.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.zm.albumpic.entity.LocalMediaFolder;
import com.zm.picture.sample.R;
import com.zm.picture.sample.mvp.ui.adapter.ImageFolderAdapter;
import com.zm.tool.library.util.ScreenUtils;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dee on 15/11/20.
 */
public class FolderPopup extends PopupWindow {
    private Context context;
    private View window;
    @BindView(R.id.folder_list)
    RecyclerView recyclerView;
    private ImageFolderAdapter adapter;
    private boolean isDismiss = false;
    public FolderPopup(Context context) {
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.folder_popup, null);
        ButterKnife.bind(this, window);
        this.setContentView(window);
        this.setWidth(ScreenUtils.getScreenWidth(context));
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.dip2px(context, 96));
        this.setAnimationStyle(R.style.WindowStyle);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(Color.argb(153, 0, 0, 0)));
        initView();
        setPopupWindowTouchModal(this, false);
    }

    public void initView() {
        adapter = new ImageFolderAdapter(context);
        recyclerView.addItemDecoration(new ItemDivider());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
    public void bindFolder(List<LocalMediaFolder> folders){
        adapter.bindFolder(folders);
    }
    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_in);
        recyclerView.startAnimation(animation);
    }
    public void setOnItemClickListener(ImageFolderAdapter.OnItemClickListener onItemClickListener){
        adapter.setOnItemClickListener(onItemClickListener);
    }
    @Override
    public void dismiss() {
        if(isDismiss){
            return;
        }
        isDismiss = true;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_out);
        recyclerView.startAnimation(animation);
        dismiss();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isDismiss = false;
                FolderPopup.super.dismiss();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {
            method = PopupWindow.class.getDeclaredMethod("setTouchModal",boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class ItemDivider extends RecyclerView.ItemDecoration {
        private Drawable mDrawable;
        public ItemDivider() {
            mDrawable = context.getResources().getDrawable(R.drawable.image_item_divider);
        }
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent) {
            final int left = ScreenUtils.dip2px(parent.getContext(),16);
            final int right = parent.getWidth() - left;

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int position, RecyclerView parent) {
            outRect.set(0, 0, 0, mDrawable.getIntrinsicWidth());
        }

    }

}
