package com.video.vip.player.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.video.vip.player.R;
import com.video.vip.player.fragment.*;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (StringUtils.isBlank(default_api)) {
            SharedPreferences sharedPreferences = getSharedPreferences(AppSettingActivity.APP_CONFIG_FILE_KEY, MODE_PRIVATE);
            default_api = sharedPreferences.getString(AppSettingActivity.DEFAULT_API_KEY, "https://jx.quankan.app/?url=");
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
            web_url = convertURL(web_url);
            mBundle.putString(AgentWebFragment.URL_KEY, default_api + web_url);
            mBundle.putBoolean(AgentWebFragment.SHOW_SEARCH_KEY, false);
            mBundle.putBoolean(AgentWebFragment.SHOW_PLAYER_KEY, false);
            mBundle.putBoolean(AgentWebFragment.SHOW_TP_KEY, false);
            String web_title = getIntent().getStringExtra(AgentWebFragment.WEB_TITLE_KEY);
            mBundle.putString(AgentWebFragment.WEB_TITLE_KEY, web_title);
        }
        ft.commit();
    }

    private String convertURL(String url) {
        if (url.contains("v.qq.com")) {
            String cid = getParm(url);
            url = "https://v.qq.com/x/cover/" + cid;
        }
        return url;
    }

    private static Pattern pCid = Pattern.compile("cid=([^&]*)");
    private static Pattern pVid = Pattern.compile("vid=([^&]*)");

    public String getParm(String urlStr) {
        String cid = "";

        Matcher mCid = pCid.matcher(urlStr);
        if (mCid.find()) {
            cid = mCid.group(1);
        }
        String pid = "";

        Matcher mVid = pVid.matcher(urlStr);
        if (mVid.find()) {
            pid = mVid.group(1);
        }
        if (StringUtils.isBlank(pid)) {
            return cid + ".html";
        }
        return cid + "/" + pid + ".html";
    }
}