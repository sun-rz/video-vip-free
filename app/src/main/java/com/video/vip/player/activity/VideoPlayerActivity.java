package com.video.vip.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.video.vip.player.R;
import com.video.vip.player.fragment.*;

import static com.video.vip.player.activity.MainActivity.*;

public class VideoPlayerActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private FrameLayout mFrameLayout;
    public static final String TYPE_KEY = "type_key";
    private FragmentManager mFragmentManager;
    private AgentWebFragment mAgentWebFragment;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);

        mFrameLayout = this.findViewById(R.id.container_framelayout);

        int key = getIntent().getIntExtra(TYPE_KEY, -1);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(FLAG_GUIDE_DICTIONARY_JS_JAVA_COMMUNICATION);
    }

    private void openFragment(int key) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;

        /*Js*/
        if (key == FLAG_GUIDE_DICTIONARY_JS_JAVA_COMMUNICATION) {
            ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
            mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/upload_file/jsuploadfile.html");
            mBundle.putBoolean(AgentWebFragment.SHOW_SEARCH_KEY, false);
            mBundle.putBoolean(AgentWebFragment.SHOW_PLAYER_KEY, false);
            String web_url = getIntent().getStringExtra(AgentWebFragment.WEB_URL_KEY);
            String web_title = getIntent().getStringExtra(AgentWebFragment.WEB_TITLE_KEY);
            mBundle.putString(AgentWebFragment.WEB_URL_KEY,web_url);
            mBundle.putString(AgentWebFragment.WEB_TITLE_KEY,web_title);
        }
        ft.commit();
    }
}
