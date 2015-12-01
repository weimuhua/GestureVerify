package com.wayne.gestureverify.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.wayne.gestureverify.GestureCallback;
import com.wayne.gestureverify.model.GesturePoint;
import com.wayne.gestureverify.utils.MobileInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawLineView extends View {

    private static final String TAG = "DrawLineView";

    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private GesturePoint mCurPoint;
    private GestureCallback mCallBack;
    private StringBuilder mPasswdSb;
    private boolean mIsVerify;
    private String mPasswd;
    private boolean mIsDrawEnable = true;

    private List<GesturePoint> mPointList;
    private Map<String, GesturePoint> mAutoCheckMap;
    private List<Pair<GesturePoint, GesturePoint>> mLineList;

    public DrawLineView(Context cxt, List<GesturePoint> pointList, boolean isVerify, String password,
            GestureCallback callback) {
        super(cxt);
        int width = MobileInfo.getScreenMetrics(getContext()).widthPixels;
        mBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.rgb(245, 142, 33));
        mPaint.setAntiAlias(true);

        mPointList = pointList;
        mLineList = new ArrayList<>();
        initAutoCheckMap();

        mIsVerify = isVerify;
        mPasswd = password;
        mCallBack = callback;
        mPasswdSb = new StringBuilder();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsDrawEnable) {
            return true;
        }

        mPaint.setColor(Color.rgb(245, 142, 33));
        int positionX = (int) event.getX();
        int positionY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurPoint = getPointAt(positionX, positionY);
                if (mCurPoint != null) {
                    mCurPoint.setState(GesturePoint.STATE_SELECTED);
                    mPasswdSb.append(mCurPoint.getNum());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                clearScreenAndDrawList();
                GesturePoint pointAt = getPointAt(positionX, positionY);
                if (pointAt == null && mCurPoint == null) {
                    return true;
                } else if (mCurPoint == null) {
                    mCurPoint = pointAt;
                    mCurPoint.setState(GesturePoint.STATE_SELECTED);
                    mPasswdSb.append(mCurPoint.getNum());
                }

                if (pointAt == null || mCurPoint.equals(pointAt)
                        || pointAt.getState() == GesturePoint.STATE_SELECTED) {
                    //尚未划到下一个点，画一条线
                    mCanvas.drawLine(mCurPoint.getCenterX(), mCurPoint.getCenterY(),
                            positionX, positionY, mPaint);
                } else {
                    //划到下一个点了，在两点间画一条线
                    mCanvas.drawLine(mCurPoint.getCenterX(), mCurPoint.getCenterY(),
                            pointAt.getCenterX(), pointAt.getCenterY(), mPaint);
                    pointAt.setState(GesturePoint.STATE_SELECTED);
                    addBetweenPointIfNeed(mCurPoint, pointAt);
                    mCurPoint = pointAt;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mIsVerify) {
                    mCallBack.onVerify(mPasswd.equals(mPasswdSb.toString()));
                } else {
                    mCallBack.onComplete(mPasswdSb.toString());
                }
                break;
        }
        return true;
    }

    private void addBetweenPointIfNeed(GesturePoint startPoint, GesturePoint endPoint) {
        //检查两个点之间是否有其他点
        GesturePoint betweenPoint = getBetweenPoint(startPoint, endPoint);
        if (betweenPoint != null && betweenPoint.getState() != GesturePoint.STATE_SELECTED) {
            Pair<GesturePoint, GesturePoint> pair1 = new Pair<>(startPoint, betweenPoint);
            mLineList.add(pair1);
            mPasswdSb.append(betweenPoint.getNum());

            Pair<GesturePoint, GesturePoint> pair2 = new Pair<>(betweenPoint, endPoint);
            mLineList.add(pair2);
            mPasswdSb.append(endPoint.getNum());

            betweenPoint.setState(GesturePoint.STATE_SELECTED);
        } else {
            Pair<GesturePoint, GesturePoint> pair = new Pair<>(startPoint, endPoint);
            mLineList.add(pair);
            mPasswdSb.append(endPoint.getNum());
        }
    }

    private GesturePoint getBetweenPoint(GesturePoint startPoint, GesturePoint endPoint) {
        int startNum = startPoint.getNum(), endNum = endPoint.getNum();
        String key;
        if (startNum < endNum) {
            key = startNum + "," + endNum;
        } else {
            key = endNum + "," + startNum;
        }
        return mAutoCheckMap.get(key);
    }

    private void clearScreenAndDrawList() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Pair<GesturePoint, GesturePoint> pair : mLineList) {
            Log.d(TAG, "clearScreenAndDrawList drawLine");
            mCanvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                    pair.second.getCenterX(), pair.second.getCenterY(), mPaint);
        }
    }

    private GesturePoint getPointAt(int x, int y) {
        for (GesturePoint point : mPointList) {
            int left = point.getLeft();
            int right = point.getRight();
            if (!(x >= left && x < right)) {
                continue;
            }

            int top = point.getTop();
            int bottom = point.getBottom();
            if (!(y >= top && y < bottom)) {
                continue;
            }
            return point;
        }
        return null;
    }

    private void initAutoCheckMap() {
        mAutoCheckMap = new HashMap<>();
        mAutoCheckMap.put("1,3", getGesturePointByNum(2));
        mAutoCheckMap.put("1,7", getGesturePointByNum(4));
        mAutoCheckMap.put("1,9", getGesturePointByNum(5));
        mAutoCheckMap.put("2,8", getGesturePointByNum(5));
        mAutoCheckMap.put("3,7", getGesturePointByNum(5));
        mAutoCheckMap.put("3,9", getGesturePointByNum(6));
        mAutoCheckMap.put("4,6", getGesturePointByNum(5));
        mAutoCheckMap.put("7,9", getGesturePointByNum(8));
    }

    private GesturePoint getGesturePointByNum(int num) {
        for (GesturePoint point : mPointList) {
            if (point.getNum() == num) {
                return point;
            }
        }
        return null;
    }

    private void drawErrorPath() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mPaint.setColor(Color.rgb(154, 7, 21));
        for (Pair<GesturePoint, GesturePoint> pair : mLineList) {
            pair.first.setState(GesturePoint.STATE_ERROR);
            pair.second.setState(GesturePoint.STATE_ERROR);
            mCanvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                    pair.second.getCenterX(), pair.second.getCenterY(), mPaint);
        }
    }

    public void cleanDrawLineView(long delayMillis) {
        if (delayMillis > 0) {
            mIsDrawEnable = false;
            drawErrorPath();
        }
        postDelayed(new clearStateRunnable(), delayMillis);
    }

    private class clearStateRunnable implements Runnable {
        @Override
        public void run() {
            mPasswdSb = new StringBuilder();
            mLineList.clear();
            for (GesturePoint point : mPointList) {
                point.setState(GesturePoint.STATE_NORMAL);
            }
            invalidate();
            mIsDrawEnable = true;
        }
    }
}
