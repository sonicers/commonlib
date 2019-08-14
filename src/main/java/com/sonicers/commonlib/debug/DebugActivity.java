package com.sonicers.commonlib.debug;

import android.os.Bundle;

import com.sonicers.commonlib.R;
import com.sonicers.commonlib.component.BaseActivity;

public class DebugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }
}
