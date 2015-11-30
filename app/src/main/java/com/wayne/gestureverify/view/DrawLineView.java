package com.wayne.gestureverify.view;

import android.content.Context;
import android.view.View;

import com.wayne.gestureverify.GestureCallback;
import com.wayne.gestureverify.model.Point;

import java.util.List;

public class DrawLineView extends View {

    public DrawLineView(Context cxt, List<Point> pointList, boolean isVerify, String password,
            GestureCallback callback) {
        super(cxt);
    }

    public void cleanDrawLineView(long delayMillis) {

    }
}
