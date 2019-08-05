package com.sonicers.commonlib.singleton;

import com.google.gson.Gson;

public class GsonUtil {
    private static Gson singleton;

    private GsonUtil() {

    }

    public static Gson getInstance() {
        if (singleton == null) {
            synchronized (GsonUtil.class) {
                if (singleton == null) {
                    singleton = new Gson();
                }
            }
        }
        return singleton;
    }
}
