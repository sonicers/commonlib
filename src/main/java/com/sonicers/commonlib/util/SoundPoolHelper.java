package com.sonicers.commonlib.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.util.HashMap;

public class SoundPoolHelper {

    private SoundPool soundPool;
    private HashMap<String, Integer> sounds = new HashMap<>();
    private Context context;

    public SoundPoolHelper(@NonNull Context context) {
        this.context = context.getApplicationContext();
        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
    }

    /**
     * 加载音频资源
     *
     * @param resId 资源ID
     */
    public void load(@NonNull String ringtoneName, @RawRes int resId) {
        sounds.put(ringtoneName, soundPool.load(context, resId, 1));
    }

    public void play(String alert) {
        if (sounds != null && sounds.containsKey(alert)) {
            Integer soundId = sounds.get(alert);
            if (soundId != null) {
                int sound = soundId;
                soundPool.play(sound, 1, 1, 0, 0, 1);
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        sounds.clear();
        if (soundPool != null) {
            soundPool.release();
        }
    }
}
