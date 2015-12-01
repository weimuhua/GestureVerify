package com.wayne.gestureverify;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.wayne.gestureverify.view.VerifyContentView;

public class SetSecCodeActivity extends Activity implements GestureCallback {

    private static final String TAG = "SetSecCodeActivity";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_sec_code_layout);
        initView();
    }

    private void initView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.gesture_container);
        VerifyContentView contentView = new VerifyContentView(this, false, "", this);
        contentView.setParentView(frameLayout);
    }

    @Override
    public void onComplete(String cipherCode) {
        if (DEBUG) Log.d(TAG, "onComplete cipherCode : " + cipherCode);
    }

    @Override
    public void onVerify(boolean state) {

    }
}
