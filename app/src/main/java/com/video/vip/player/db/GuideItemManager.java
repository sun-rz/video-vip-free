package com.video.vip.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.video.vip.player.R;
import com.video.vip.player.common.GuideItemEntity;

import static com.video.vip.player.activity.MainActivity.FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH;
import static com.video.vip.player.activity.MainActivity.FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH;

/**
 * 首页列表
 */
public class GuideItemManager extends DBManager<GuideItemEntity> {

    public GuideItemManager(Context context) {
        super(context, tableName);
    }

    private static final String tableName = "guide_item_manager";

    //主键
    private static final String ID = "id";
    //标题
    private static final String GUIDETITLE = "guideTitle";
    //id
    private static final String GUIDEDICTIONARY = "guideDictionary";
    //类型
    private static final String EXTRA = "extra";
    //图标
    private static final String IMAGE = "image";
    //链接地址
    private static final String URL = "url";

    /**
     * 建表语句
     */
    public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ( id INTEGER NOT NULL CONSTRAINT " + tableName + "_pk PRIMARY KEY AUTOINCREMENT, " + GUIDETITLE + " TEXT, " + GUIDEDICTIONARY + " INTEGER, " + EXTRA + " INTEGER, " + IMAGE + " INTEGER, " + URL + " TEXT)";

    @Override
    public void initData(SQLiteDatabase db) {

        GuideItemEntity[] guideItemEntityList = new GuideItemEntity[]{
                new GuideItemEntity("爱奇艺视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.iqy, "https://www.iqiyi.com/"),
                new GuideItemEntity("腾讯视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.txsp, "https://v.qq.com/"),
                new GuideItemEntity("乐视视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.lssp, "http://www.le.com/"),
                new GuideItemEntity("芒果TV", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.mgtv, "https://www.mgtv.com/"),
                new GuideItemEntity("优酷视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.yksp, "https://youku.com/"),
                new GuideItemEntity("PPTV", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.pptv, "https://www.pptv.com/"),
                new GuideItemEntity("搜狐视频", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH, R.mipmap.shsp, "https://tv.sohu.com/"),
                //new GuideItemEntity("奈落影视", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH, 1, R.mipmap.nl_logo, "https://www.newfii.com/"),
               // new GuideItemEntity("BL影院", FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH, 1, R.mipmap.bljiex, "https://svip.bljiex.cc/mov/"),
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

        for (GuideItemEntity guideItemEntity : guideItemEntityList) {
            // insert into db
            long add = add(guideItemEntity, db);
        }
    }


    public long add(GuideItemEntity guideItemEntity) {
        ContentValues values = new ContentValues();
        values.put(GUIDETITLE, guideItemEntity.getGuideTitle());
        values.put(GUIDEDICTIONARY, guideItemEntity.getGuideDictionary());
        values.put(EXTRA, guideItemEntity.getExtra());
        values.put(IMAGE, guideItemEntity.getImage());
        values.put(URL, guideItemEntity.getUrl());

        return insert(values);
    }

    private long add(GuideItemEntity guideItemEntity, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(GUIDETITLE, guideItemEntity.getGuideTitle());
        values.put(GUIDEDICTIONARY, guideItemEntity.getGuideDictionary());
        values.put(EXTRA, guideItemEntity.getExtra());
        values.put(IMAGE, guideItemEntity.getImage());
        values.put(URL, guideItemEntity.getUrl());

        return insert(values, db);
    }

    @Override
    public GuideItemEntity getEntity(Cursor c) {
        return new GuideItemEntity(
                c.getInt(c.getColumnIndex(ID)),
                c.getString(c.getColumnIndex(GUIDETITLE)),
                c.getInt(c.getColumnIndex(GUIDEDICTIONARY)),
                c.getInt(c.getColumnIndex(EXTRA)),
                c.getInt(c.getColumnIndex(IMAGE)),
                c.getString(c.getColumnIndex(URL))
        );
    }
}
