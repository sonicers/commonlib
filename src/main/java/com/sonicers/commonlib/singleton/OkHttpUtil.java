package com.sonicers.commonlib.singleton;

import okhttp3.OkHttpClient;

public class OkHttpUtil {
    private static OkHttpClient singleton;

    private OkHttpUtil() {

    }

    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkHttpUtil.class) {
                if (singleton == null) {
                    singleton = new OkHttpClient();
                }
            }
        }
        return singleton;
    }
}
