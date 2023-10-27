package com.video.vip.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.video.vip.player.bean.ApiURLConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


/**
 * api接口配置
 *
 * @author scd
 */

public class AppSettingManager extends DBManager<ApiURLConfig> {
    Context mContext;

    public AppSettingManager(Context context) {
        super(context, tableName);
        this.mContext = context;
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

        InputStream is = null;
        try {
            is = mContext.getAssets().open("conf/api.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = new String(line.getBytes(), StandardCharsets.UTF_8);
                lines.append(line);
            }
            reader.close();
            Gson gson = new Gson();
            ApiURLConfig[] apiURLConfigs = gson.fromJson(lines.toString(), ApiURLConfig[].class);
            if (null != apiURLConfigs) {
                for (ApiURLConfig apiURLConfig : apiURLConfigs) {
                    add(apiURLConfig, db);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public long add(ApiURLConfig apiURLConfig) {
        ContentValues values = new ContentValues();
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

