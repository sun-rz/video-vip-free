package com.video.vip.player.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.video.vip.player.R;
import com.video.vip.player.bean.ApiURLConfig;
import com.video.vip.player.db.AppSettingManager;
import com.video.vip.player.fragment.*;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.video.vip.player.activity.MainActivity.*;

public class VideoPlayerActivity extends AppCompatActivity {
    private FrameLayout mFrameLayout;
    private FragmentManager mFragmentManager;
    private AgentWebFragment mAgentWebFragment;
    public static ApiURLConfig defaultApi;

    public VideoPlayerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);

        if (null == defaultApi) {
            AppSettingManager appSettingManager = new AppSettingManager(getApplicationContext());
            defaultApi = appSettingManager.queryByField("is_default = ?", new String[]{"1"});
            if (defaultApi == null) {
                defaultApi = appSettingManager.queryByField("is_default = ?", new String[]{"0"});
            }
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
            mBundle.putString(AgentWebFragment.URL_KEY, defaultApi.getCode() + web_url);
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
        } else if (url.contains("iqiyi.com")) {
            url = url.replace("m.iqiyi.com", "iqiyi.com");
            /*Matcher matcher = iCover.matcher(url);
            if (matcher.find()) {
                url = matcher.group(1);
            }*/
        } else if (url.contains("m.le.com")) {
            url = url.replace("m.le.com", "le.com");
        } else if (url.contains("m.mgtv.com")) {
            url = url.replace("m.mgtv.com", "mgtv.com");
        }
        return url;
    }

    private static Pattern pCid = Pattern.compile("cid=([^&]*)");
    private static Pattern pVid = Pattern.compile("vid=([^&]*)");
    private static Pattern pCover = Pattern.compile(".*cover/s/(.*.html)");
    private static Pattern iCover = Pattern.compile("(.*)\\?.*");

    public String getParm(String urlStr) {
        Matcher matcher = pCover.matcher(urlStr);
        if (matcher.find()) {
            return matcher.group(1);
        }

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