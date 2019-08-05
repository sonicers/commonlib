package com.sonicers.commonlib.util;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

public class FullScreenUtil {

    /**
     * 隐藏状态栏，并为SYSTEM_UI_FLAG_IMMERSIVE_STICKY模式，需要配合主题属性windowTranslucentStatus使用
     */
    public static void initFullScreen(@NonNull Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
