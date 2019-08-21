package com.sonicers.commonlib.debug;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sonicers.commonlib.R;
import com.sonicers.commonlib.component.BaseActivity;

public class DebugActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        mBtnUpload = findViewById(R.id.btn_upload);
        mBtnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mBtnUpload == v) {

        }
    }
}
