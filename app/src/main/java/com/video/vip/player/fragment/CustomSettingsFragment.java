package com.video.vip.player.fragment;

import android.os.Bundle;
import com.just.agentweb.IAgentWebSettings;
import com.video.vip.player.common.CustomSettings;

/**
 * Created by cenxiaozhong on 2017/5/26.
 * source code  https://github.com/Justson/AgentWeb
 */

public class CustomSettingsFragment extends AgentWebFragment {

    public static AgentWebFragment getInstance(Bundle bundle) {

        CustomSettingsFragment mCustomSettingsFragment = new CustomSettingsFragment();
        if (bundle != null){
            mCustomSettingsFragment.setArguments(bundle);
        }
        return mCustomSettingsFragment;

    }

    @Override
    public IAgentWebSettings getSettings() {
        return new CustomSettings(getActivity());
    }
}
