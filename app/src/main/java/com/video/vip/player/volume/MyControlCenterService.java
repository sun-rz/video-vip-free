package com.video.vip.player.volume;

import android.content.Context;
import android.media.AudioManager;
import android.service.quicksettings.TileService;

public class MyControlCenterService extends TileService {
    @Override
    public void onClick() {
        super.onClick();
        // 在这里定义单击组件时执行的操作
        // 调整音量的逻辑
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume <= 1) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    }
}