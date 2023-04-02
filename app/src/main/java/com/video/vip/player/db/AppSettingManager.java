package com.video.vip.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.video.vip.player.bean.ApiURLConfig;


/**
 * api接口配置
 *
 * @author scd
 */

public class AppSettingManager extends DBManager<ApiURLConfig> {

    public AppSettingManager(Context context) {
        super(context, tableName);
    }

    private static final String tableName = "api_url_config";

    //id
    private static final String ID = "id";
    //接口地址
    private static final String CODE = "code";
    //接口名称
    private static final String TITLE = "title";
    //是否默认
    private static final String IS_DEFAULT = "is_default";

    public final static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ( id INTEGER NOT NULL CONSTRAINT " + tableName + "_pk PRIMARY KEY AUTOINCREMENT, " + CODE + " TEXT, " + TITLE + " TEXT, " + IS_DEFAULT + " INTEGER )";

    @Override
    public void initData(SQLiteDatabase db) {

        ApiURLConfig[] apiURLConfigs = {
                new ApiURLConfig("http://jx.aidouer.net/?url=", "视频云解析【全网解析】", 1),
                new ApiURLConfig("https://jx.quankan.app/?url=", "麒麟播放器", 0),
                new ApiURLConfig("https://jx.m3u8.tv/jiexi/?url=", "M3U8.TV无广告JSON解析", 0),
                new ApiURLConfig("https://dmjx.m3u8.tv/?url=", "M3U8.TV无广告弹幕解析", 0),
                new ApiURLConfig("https://im1907.top/?jx=", "M1907云加速在线播放器", 0),
                new ApiURLConfig("https://jx.xmflv.com/?url=", "虾米解析", 0),
                new ApiURLConfig("http://api.apii.top/?v=", "全网VIP会员视频解析", 0),
                new ApiURLConfig("https://jy.we-vip.com:2053/?url=", "PlayerJy解析", 0),
                new ApiURLConfig("https://svip.bljiex.cc/?v=", "BL解析", 0),
                //new ApiURLConfig("http://jiexi44.qmbo.cn/jiexi/?url=", "云解析资源网", 0),
                //new ApiURLConfig("https://www.nxflv.com/?url=", "诺讯智能解析系统", 0),
                new ApiURLConfig("https://jx.iztyy.com/svip/?url=", "猪蹄解析", 0),
               // new ApiURLConfig("https://video.isyour.love/player/getplayer?&adadress=https://jx.cjw123.com/ad.html?&url=", "邦宁云播播放器", 0),
                new ApiURLConfig("https://okjx.cc/?url=", "OK解析", 0),
                new ApiURLConfig("https://bd.jx.cn/?url=", "冰豆解析", 0),
                new ApiURLConfig("https://jx.4kdv.com/?url=", "4K解析", 0)
        };
        for (ApiURLConfig apiURLConfig : apiURLConfigs) {
            add(apiURLConfig, db);
        }
    }

    private long add(ApiURLConfig apiURLConfig) {
        ContentValues values = new ContentValues();
        //values.put(ID, apiURLConfig.getId());
        values.put(CODE, apiURLConfig.getCode());
        values.put(TITLE, apiURLConfig.getTitle());
        values.put(IS_DEFAULT, apiURLConfig.getIs_default());

        return insert(values);
    }


    private long add(ApiURLConfig apiURLConfig, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CODE, apiURLConfig.getCode());
        values.put(TITLE, apiURLConfig.getTitle());
        values.put(IS_DEFAULT, apiURLConfig.getIs_default());
        return insert(values, db);
    }

    @Override
    public ApiURLConfig getEntity(Cursor c) {
        return new ApiURLConfig(
                c.getInt(c.getColumnIndex(ID)),
                c.getString(c.getColumnIndex(CODE)),
                c.getString(c.getColumnIndex(TITLE)),
                c.getInt(c.getColumnIndex(IS_DEFAULT))
        );
    }
}

