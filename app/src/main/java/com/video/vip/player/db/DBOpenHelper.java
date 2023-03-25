package com.video.vip.player.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 * 
 * @author scd
 * 
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    // 构造方法
    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
        mContext = context;
    }

    // 版本号
    private static final int VERSION = 200;
    // 数据库名称
    private static final String DBNAME ="video_free.db";
    private Context mContext;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 运行创建表语句
        db.execSQL(GuideItemManager.CREATE_TABLE_SQL);
        db.execSQL(AppSettingManager.CREATE_TABLE_SQL);
        // 初始化数据
        new GuideItemManager(mContext).initData(db);
        new AppSettingManager(mContext).initData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        onCreate(db);
    }
}
