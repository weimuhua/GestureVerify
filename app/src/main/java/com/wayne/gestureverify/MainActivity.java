package com.wayne.gestureverify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSetBtn;
    private Button mVerifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mSetBtn = (Button) findViewById(R.id.set_sec_code);
        mVerifyBtn = (Button) findViewById(R.id.verify_sec_code);
        mSetBtn.setOnClickListener(this);
        mVerifyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == mSetBtn) {
            intent = new Intent(this, SetSecCodeActivity.class);
        } else if (v == mVerifyBtn) {
            intent = new Intent(this, VerifySecCodeActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
