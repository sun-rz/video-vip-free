package com.video.vip.player.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.flyingpigeon.library.ServiceManager;
import com.flyingpigeon.library.annotations.thread.MainThread;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.just.agentweb.AgentWebConfig;
import com.video.vip.player.R;
import com.video.vip.player.api.Api;
import com.video.vip.player.bean.SelectItem;
import com.video.vip.player.common.GuideItemEntity;
import com.video.vip.player.fragment.AgentWebFragment;
import com.video.vip.player.widget.MyGridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.video.vip.player.activity.AppSettingActivity.*;
import static com.video.vip.player.sonic.SonicJavaScriptInterface.PARAM_CLICK_TIME;

/**
 * source code  https://github.com/Justson/AgentWeb
 */
public class MainActivity extends AppCompatActivity {


    private ListView mListView;

    private Toolbar mToolbar;
    private TextView mTitleTextView;
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int FLAG_GUIDE_DICTIONARY_USE_IN_ACTIVITY = 0x01;
    public static final int FLAG_GUIDE_DICTIONARY_USE_IN_FRAGMENT = FLAG_GUIDE_DICTIONARY_USE_IN_ACTIVITY << 1;
    public static final int FLAG_GUIDE_DICTIONARY_FILE_DOWNLOAD = FLAG_GUIDE_DICTIONARY_USE_IN_FRAGMENT << 1;
    public static final int FLAG_GUIDE_DICTIONARY_INPUT_TAG_PROBLEM = FLAG_GUIDE_DICTIONARY_FILE_DOWNLOAD << 1;
    public static final int FLAG_GUIDE_DICTIONARY_JS_JAVA_COMMUNICATION = FLAG_GUIDE_DICTIONARY_INPUT_TAG_PROBLEM << 1;
    public static final int FLAG_GUIDE_DICTIONARY_VIDEO_FULL_SCREEN = FLAG_GUIDE_DICTIONARY_JS_JAVA_COMMUNICATION << 1;
    public static final int FLAG_GUIDE_DICTIONARY_CUSTOM_PROGRESSBAR = FLAG_GUIDE_DICTIONARY_VIDEO_FULL_SCREEN << 1;
    public static final int FLAG_GUIDE_DICTIONARY_CUSTOM_WEBVIEW_SETTINGS = FLAG_GUIDE_DICTIONARY_CUSTOM_PROGRESSBAR << 1;
    public static final int FLAG_GUIDE_DICTIONARY_LINKS = FLAG_GUIDE_DICTIONARY_CUSTOM_WEBVIEW_SETTINGS << 1;
    public static final int FLAG_GUIDE_DICTIONARY_BOUNCE_EFFACT = FLAG_GUIDE_DICTIONARY_LINKS << 1;
    public static final int FLAG_GUIDE_DICTIONARY_JSBRIDGE_SAMPLE = FLAG_GUIDE_DICTIONARY_BOUNCE_EFFACT << 1;
    public static final int FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_ACT = FLAG_GUIDE_DICTIONARY_JSBRIDGE_SAMPLE << 1;
    public static final int FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_FRAG = FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_ACT << 1;
    public static final int FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH = FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_FRAG << 1;
    public static final int FLAG_GUIDE_DICTIONARY_MAP = FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH << 1;
    public static final int FLAG_GUIDE_DICTIONARY_VASSONIC_SAMPLE = FLAG_GUIDE_DICTIONARY_MAP << 1;
    public static final int FLAG_GUIDE_DICTIONARY_LINKAGE_WITH_TOOLBAR = FLAG_GUIDE_DICTIONARY_VASSONIC_SAMPLE << 1;
    public static final int FLAG_GUIDE_DICTIONARY_CUTSTOM_WEBVIEW = FLAG_GUIDE_DICTIONARY_LINKAGE_WITH_TOOLBAR << 1;
    public static final int FLAG_GUIDE_DICTIONARY_JS_JAVA_COMUNICATION_UPLOAD_FILE = FLAG_GUIDE_DICTIONARY_CUTSTOM_WEBVIEW << 1;
    public static final int FLAG_GUIDE_DICTIONARY_COMMON_FILE_DOWNLOAD = FLAG_GUIDE_DICTIONARY_JS_JAVA_COMUNICATION_UPLOAD_FILE << 1;
    public static final int FLAG_GUIDE_DICTIONARY_IPC = FLAG_GUIDE_DICTIONARY_COMMON_FILE_DOWNLOAD << 1;
    public static final int FLAG_GUIDE_DICTIONARY_WEBRTC = FLAG_GUIDE_DICTIONARY_IPC << 1;
    public static final int FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH = FLAG_GUIDE_DICTIONARY_WEBRTC << 1;


    public static final GuideItemEntity[] datas = new GuideItemEntity[]{
            new GuideItemEntity("爱奇艺视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.iqy, "https://www.iqiyi.com/"),
            new GuideItemEntity("腾讯视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.txsp, "https://v.qq.com/"),
            new GuideItemEntity("乐视视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.lssp, "http://www.le.com/"),
            new GuideItemEntity("芒果TV", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.mgtv, "https://www.mgtv.com/"),
            new GuideItemEntity("优酷视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.yksp, "https://youku.com/"),
            new GuideItemEntity("PPTV", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.pptv, "https://www.pptv.com/"),
            new GuideItemEntity("搜狐视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.shsp, "https://tv.sohu.com/#_pageb"),
            new GuideItemEntity("百度", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH, R.mipmap.baidu, "https://www.baidu.com/"),
            new GuideItemEntity("必应", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH, R.mipmap.by, "https://cn.bing.com/"),
            /*new GuideItemEntity("SmartRefresh 下拉刷新", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH,R.mipmap.app_logo),
            new GuideItemEntity("Activity 使用 AgentWeb", FLAG_GUIDE_DICTIONARY_USE_IN_ACTIVITY,R.mipmap.app_logo),
            new GuideItemEntity("Fragment 使用 AgentWeb ", FLAG_GUIDE_DICTIONARY_USE_IN_FRAGMENT,R.mipmap.app_logo),
            new GuideItemEntity("IPC WebView独立进程", FLAG_GUIDE_DICTIONARY_IPC,R.mipmap.app_logo),
            new GuideItemEntity("WebRTC 使用", FLAG_GUIDE_DICTIONARY_WEBRTC,R.mipmap.app_logo),
            new GuideItemEntity("H5文件下载", FLAG_GUIDE_DICTIONARY_FILE_DOWNLOAD,R.mipmap.app_logo),
            new GuideItemEntity("input标签文件上传", FLAG_GUIDE_DICTIONARY_INPUT_TAG_PROBLEM,R.mipmap.app_logo),
            new GuideItemEntity("Js 通信文件上传,兼用Android 4.4Kitkat", FLAG_GUIDE_DICTIONARY_JS_JAVA_COMUNICATION_UPLOAD_FILE,R.mipmap.app_logo),
            new GuideItemEntity("Js 通信", FLAG_GUIDE_DICTIONARY_JS_JAVA_COMMUNICATION,R.mipmap.app_logo),
            new GuideItemEntity("Video 视频全屏播放", FLAG_GUIDE_DICTIONARY_VIDEO_FULL_SCREEN,R.mipmap.app_logo),
            new GuideItemEntity("自定义进度条", FLAG_GUIDE_DICTIONARY_CUSTOM_PROGRESSBAR,R.mipmap.app_logo),
            new GuideItemEntity("自定义设置", FLAG_GUIDE_DICTIONARY_CUSTOM_WEBVIEW_SETTINGS,R.mipmap.app_logo),
            new GuideItemEntity("电话 ， 信息 ， 邮件", FLAG_GUIDE_DICTIONARY_LINKS,R.mipmap.app_logo),
            new GuideItemEntity("自定义 WebView", FLAG_GUIDE_DICTIONARY_CUTSTOM_WEBVIEW,R.mipmap.app_logo),
            new GuideItemEntity("下拉回弹效果", FLAG_GUIDE_DICTIONARY_BOUNCE_EFFACT,R.mipmap.app_logo),
            new GuideItemEntity("Jsbridge 例子", FLAG_GUIDE_DICTIONARY_JSBRIDGE_SAMPLE,R.mipmap.app_logo),
            new GuideItemEntity("继承 BaseAgentWebActivity", FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_ACT,R.mipmap.app_logo),
            new GuideItemEntity("继承 BaseAgentWebFragment", FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_FRAG,R.mipmap.app_logo),
            new GuideItemEntity("地图", FLAG_GUIDE_DICTIONARY_MAP,R.mipmap.app_logo),
            new GuideItemEntity("VasSonic 首屏秒开", FLAG_GUIDE_DICTIONARY_VASSONIC_SAMPLE,R.mipmap.app_logo),
            new GuideItemEntity("与ToolBar联动", FLAG_GUIDE_DICTIONARY_LINKAGE_WITH_TOOLBAR,R.mipmap.app_logo),
            new GuideItemEntity("原生文件下载", FLAG_GUIDE_DICTIONARY_COMMON_FILE_DOWNLOAD,R.mipmap.app_logo),*/
    };


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = this.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        mTitleTextView = this.findViewById(R.id.toolbar_title);
        mTitleTextView.setText("vip浏览器");
        this.setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> {
            MainActivity.this.finish();
        });

        MyGridView gridView = this.findViewById(R.id.main_gridview);

        gridView.setAdapter(new MyGridViewAdapter(this));

        gridView.setOnItemClickListener((adapterView, view, position, id) -> doClick(position));

        if (AgentWebConfig.DEBUG) {
            Log.i("Info", "Debug 模式");
        } else {
            Log.i("Info", "release 模式");
        }

        AgentWebConfig.debug();
        ServiceManager.getInstance().publish(mApi);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_CONFIG_FILE_KEY, MODE_PRIVATE);
        int api_version = sharedPreferences.getInt(API_VERSION_KEY, 0);
        if (api_version < 1) {
            SharedPreferences.Editor editor = getSharedPreferences(APP_CONFIG_FILE_KEY, MODE_PRIVATE).edit();
            editor.putInt(API_VERSION_KEY, 1);
            editor.putString(DEFAULT_API_KEY, "https://jx.quankan.app/?url=");
            editor.apply();
            FileOutputStream outputStream = null;
            try {
                outputStream = this.openFileOutput(CONFIG_FILE_KEY, Context.MODE_PRIVATE);
                outputStream.write(JSONCONF.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e("AppSettingActivity", "写入配置文件出错", e);
                    }
                }
            }
        }
    }

    private Api mApi = new Api() {

        @MainThread
        // default callback on the bind Thread , if you wanna it callback on mainThread , may be you should add MainThread annotation
        @Override
        public void onReady() {
            Log.e(TAG, "web process onReady, i am runing on main process , received web procecss onready signal.");
        }
    };

    private void doClick(int position) {
        GuideItemEntity guideItemEntity = datas[position];
        int index = guideItemEntity.getGuideDictionary();
        switch (index) {
            case FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH)
                        .putExtra(AgentWebFragment.WEB_URL_KEY, guideItemEntity.getUrl())
                );
                break;

            case FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH)
                        .putExtra(AgentWebFragment.WEB_URL_KEY, guideItemEntity.getUrl())
                );
                break;

            /* Activity agentWeb */
            case FLAG_GUIDE_DICTIONARY_USE_IN_ACTIVITY:

                startActivity(new Intent(this, WebActivity.class));
                break;
            case FLAG_GUIDE_DICTIONARY_USE_IN_FRAGMENT:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_USE_IN_FRAGMENT));
                break;
            case FLAG_GUIDE_DICTIONARY_FILE_DOWNLOAD:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_FILE_DOWNLOAD));
                break;
            case FLAG_GUIDE_DICTIONARY_WEBRTC:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_WEBRTC));
                break;
            case FLAG_GUIDE_DICTIONARY_INPUT_TAG_PROBLEM:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_INPUT_TAG_PROBLEM));
                break;
            case FLAG_GUIDE_DICTIONARY_JS_JAVA_COMUNICATION_UPLOAD_FILE:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_JS_JAVA_COMUNICATION_UPLOAD_FILE));
                break;
            case FLAG_GUIDE_DICTIONARY_JS_JAVA_COMMUNICATION:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_JS_JAVA_COMMUNICATION));
                break;
            case FLAG_GUIDE_DICTIONARY_VIDEO_FULL_SCREEN:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_VIDEO_FULL_SCREEN));
                break;

            case FLAG_GUIDE_DICTIONARY_CUSTOM_PROGRESSBAR:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_CUSTOM_PROGRESSBAR));
                break;
            case FLAG_GUIDE_DICTIONARY_CUSTOM_WEBVIEW_SETTINGS:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_CUSTOM_WEBVIEW_SETTINGS));
                break;
            case FLAG_GUIDE_DICTIONARY_LINKS:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_LINKS));
                break;
            case FLAG_GUIDE_DICTIONARY_CUTSTOM_WEBVIEW:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_CUTSTOM_WEBVIEW));
                break;
            case FLAG_GUIDE_DICTIONARY_BOUNCE_EFFACT:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_BOUNCE_EFFACT));
                break;
            case FLAG_GUIDE_DICTIONARY_JSBRIDGE_SAMPLE:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_JSBRIDGE_SAMPLE));
                break;
            case FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_ACT:
                startActivity(new Intent(this, EasyWebActivity.class));
                break;

            case FLAG_GUIDE_DICTIONARY_EXTENDS_BASE_FRAG:
                startActivity(new Intent(this, ContainerActivity.class));
                break;
            case FLAG_GUIDE_DICTIONARY_MAP:
                startActivity(new Intent(this, CommonActivity.class)
                        .putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_MAP));
                break;
            case FLAG_GUIDE_DICTIONARY_VASSONIC_SAMPLE:
                startActivity(new Intent(this,
                        CommonActivity.class).putExtra(CommonActivity.TYPE_KEY, FLAG_GUIDE_DICTIONARY_VASSONIC_SAMPLE)
                        .putExtra(PARAM_CLICK_TIME, System.currentTimeMillis()));
                break;
            case FLAG_GUIDE_DICTIONARY_LINKAGE_WITH_TOOLBAR:
                startActivity(new Intent(this, AutoHidenToolbarActivity.class));
                break;
            case FLAG_GUIDE_DICTIONARY_COMMON_FILE_DOWNLOAD:
                startActivity(new Intent(this, NativeDownloadActivity.class));
                break;
            case FLAG_GUIDE_DICTIONARY_IPC:
                startActivity(new Intent(this, RemoteWebViewlActivity.class).putExtra(AgentWebFragment.URL_KEY, "https://m.vip.com/?source=www&jump_https=1"));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceManager.getInstance().unpublish(mApi);
        File dir = getApplicationContext().getCacheDir();
        if (dir != null && dir.isDirectory()) {
            deleteDir(dir);
        }
        System.exit(0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    /**
     * 自定义Adapter
     * Created by Spring on 2015/11/28.
     */
    public static class MyGridViewAdapter extends BaseAdapter {
        private Context context;

        public MyGridViewAdapter(Context context) {
            super();
            this.context = context;
        }

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyGridViewAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
                holder = new ViewHolder();
                holder.imageView = convertView.findViewById(R.id.item_img);
                holder.textView = convertView.findViewById(R.id.item_txt);
                convertView.setTag(holder);
            } else {
                holder = (MyGridViewAdapter.ViewHolder) convertView.getTag();
            }
            GuideItemEntity guideItemEntity = datas[position];
            holder.imageView.setBackgroundResource(guideItemEntity.getImage());
            holder.textView.setText(guideItemEntity.getGuideTitle());

            return convertView;
        }

        static class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null) {
                return true;
            }
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        if (dir == null) {
            return true;
        }
        return dir.delete();
    }
}
