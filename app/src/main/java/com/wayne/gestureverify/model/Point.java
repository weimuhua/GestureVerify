package com.wayne.gestureverify.model;

import android.widget.ImageView;

import com.wayne.gestureverify.R;

public class Point {

    public static final int STATE_NORMAL = 1;
    public static final int STATE_SELECTED = 2;
    public static final int STATE_ERROR = 3;

    /** 左下角距离父view的距离 */
    private int left;
    /** 左上角距离父view的距离 */
    private int top;
    /** 右上角距离父view的距离 */
    private int right;
    /** 右下角距离父view的距离 */
    private int bottom;
    /** 位置标记，1-9 */
    private int num;
    /** 中间的图片 */
    private ImageView imageView;
    /** 当前的状态 */
    private int state;

    public Point(int left, int top, int right, int bottom, int num, ImageView image, int state) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.num = num;
        this.imageView = image;
        this.state = state;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case STATE_NORMAL:
                imageView.setBackgroundResource(R.mipmap.gesture_node_normal);
                break;
            case STATE_SELECTED:
                imageView.setBackgroundResource(R.mipmap.gesture_node_pressed);
                break;
            case STATE_ERROR:
                imageView.setBackgroundResource(R.mipmap.gesture_node_wrong);
                break;
            default:
                break;
        }
    }
}
