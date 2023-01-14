package com.video.vip.player.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.video.vip.player.R;
import com.video.vip.player.fragment.*;
import com.video.vip.player.utils.StringUitls;

import static com.video.vip.player.activity.MainActivity.*;

public class VideoPlayerActivity extends AppCompatActivity {
    private FrameLayout mFrameLayout;
    private FragmentManager mFragmentManager;
    private AgentWebFragment mAgentWebFragment;
    public static String default_api;

    public VideoPlayerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);

        if(StringUitls.isBlank(default_api)) {
            SharedPreferences sharedPreferences = getSharedPreferences(AppSettingActivity.APP_CONFIG_FILE_KEY, MODE_PRIVATE);
            default_api = sharedPreferences.getString(AppSettingActivity.DEFAULT_API_KEY, "https://jx.aidouer.net/?url=");
        }

        mFrameLayout = this.findViewById(R.id.container_framelayout);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(FLAG_GUIDE_DICTIONARY_VIDEO_FULL_SCREEN);
    }

    private void openFragment(int key) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle;

        /*Js*/
        if (key == FLAG_GUIDE_DICTIONARY_VIDEO_FULL_SCREEN) {
            ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
            String web_url = getIntent().getStringExtra(AgentWebFragment.WEB_URL_KEY);
            mBundle.putString(AgentWebFragment.URL_KEY, default_api + web_url);
            mBundle.putBoolean(AgentWebFragment.SHOW_SEARCH_KEY, false);
            mBundle.putBoolean(AgentWebFragment.SHOW_PLAYER_KEY, false);
            String web_title = getIntent().getStringExtra(AgentWebFragment.WEB_TITLE_KEY);
            mBundle.putString(AgentWebFragment.WEB_TITLE_KEY, web_title);
        }
        ft.commit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            if ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                // 是全屏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
        super.onConfigurationChanged(newConfig);
    }
}