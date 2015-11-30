package com.wayne.gestureverify.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wayne.gestureverify.GestureCallback;
import com.wayne.gestureverify.R;
import com.wayne.gestureverify.model.Point;
import com.wayne.gestureverify.utils.MobileInfo;

import java.util.ArrayList;
import java.util.List;

public class VerifyContentView extends ViewGroup {

    private static final int HORIZONTAL_POINTS_COUNT = 3;
    private static final int HORIZONTAL_POINT_GAP_NUM = HORIZONTAL_POINTS_COUNT * 2;
    private static final int VERTICAL_POINTS_COUNT = 3;
    private static final int VERTICAL_POINT_GAP_NUM = VERTICAL_POINTS_COUNT * 2;
    private static final int TOTAL_POINTS_COUNT = HORIZONTAL_POINTS_COUNT * VERTICAL_POINT_GAP_NUM;

    private int mPointLength;
    private Context mContext;
    private boolean isVerify;
    private DrawLineView mDrawLineView;
    private List<Point> mPointList;

    public VerifyContentView(Context cxt, boolean isVerify, String password, GestureCallback callback) {
        super(cxt);
        mContext = cxt;
        this.isVerify = isVerify;
        initChildList();
        mDrawLineView = new DrawLineView(mContext, mPointList, isVerify, password, callback);
        mPointLength = MobileInfo.getScreenMetrics(mContext).widthPixels / HORIZONTAL_POINTS_COUNT;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            int row = i / 3;
            int column = i % 3;
            View v = getChildAt(i);
            v.layout(column * mPointLength + mPointLength / HORIZONTAL_POINT_GAP_NUM,
                    row * mPointLength + mPointLength / VERTICAL_POINT_GAP_NUM,
                    (column + 1) * mPointLength - mPointLength / HORIZONTAL_POINT_GAP_NUM,
                    (row + 1) * mPointLength - mPointLength / VERTICAL_POINT_GAP_NUM);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void initChildList() {
        mPointList = new ArrayList<>();
        for (int i = 0; i < TOTAL_POINTS_COUNT; i++) {
            ImageView image = new ImageView(mContext);
            image.setBackgroundResource(R.mipmap.gesture_node_normal);
            addView(image);
            invalidate();

            int row = i / 3;
            int column = i % 3;
            int left = column * mPointLength + mPointLength / HORIZONTAL_POINT_GAP_NUM;
            int top = row * mPointLength + mPointLength / VERTICAL_POINT_GAP_NUM;
            int right = (column + 1) * mPointLength - mPointLength / HORIZONTAL_POINT_GAP_NUM;
            int bottom = (row + 1) * mPointLength - mPointLength / VERTICAL_POINT_GAP_NUM;
            Point p = new Point(left, top, right, bottom, i + 1, image);
            mPointList.add(p);
        }
    }

    public void addDrawLineView(ViewGroup parent) {
        int with = MobileInfo.getScreenMetrics(mContext).widthPixels;
        LayoutParams params = new LayoutParams(with, with);
        setLayoutParams(params);
        mDrawLineView.setLayoutParams(params);
        parent.addView(mDrawLineView);
    }

    public void clearDrawLineView(long delayMillis) {
        mDrawLineView.cleanDrawLineView(delayMillis);
    }
}
