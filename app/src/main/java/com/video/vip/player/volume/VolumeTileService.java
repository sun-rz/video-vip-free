package com.video.vip.player.volume;

import android.content.Context;
import android.media.AudioManager;
import android.service.quicksettings.TileService;

public class VolumeTileService extends TileService {

    @Override
    public void onClick() {
        super.onClick();

        // 调整音量的逻辑
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }
}