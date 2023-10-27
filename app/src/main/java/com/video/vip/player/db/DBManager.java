package com.video.vip.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager<T> {

    public Context mContext;
    public DBOpenHelper mDBHelper;
    //表名
    public String TABLE_NAME;

    public DBManager(Context context, String table_name) {
        mContext = context;
        TABLE_NAME = table_name;
    }

    /**
     * 创建数据库
     *
     * @return
     */
    public SQLiteDatabase getDatabase() {
        if (mDBHelper == null) {
            mDBHelper = new DBOpenHelper(mContext);
        }
        return mDBHelper.getWritableDatabase();
    }

    /**
     * 初始化数据
     *
     * @param db
     */
    public void initData(SQLiteDatabase db) {

    }

    /**
     * 添加数据
     *
     * @param values
     * @return
     */

    public long insert(ContentValues values) {
        SQLiteDatabase db = getDatabase();
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return insert;
    }

    public long insert(ContentValues values, SQLiteDatabase db) {
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * 更新数据
     *
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getDatabase();
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs, SQLiteDatabase db) {
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * 删除数据
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getDatabase();
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public int delete(String whereClause, String[] whereArgs, SQLiteDatabase db) {
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    /**
     * 查询出全部数据
     */
    public List<T> query() {
        List<T> list = new ArrayList<>();
        SQLiteDatabase db = getDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c != null && c.getCount() > 0 && c.moveToFirst()) {
            do {
                list.add(getEntity(c));
            } while (c.moveToNext());

            c.close();
        }
        return list;
    }

    public List<T> query(SQLiteDatabase db) {
        List<T> list = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c != null && c.getCount() > 0 && c.moveToFirst()) {
            do {
                list.add(getEntity(c));
            } while (c.moveToNext());

            c.close();
        }
        return list;
    }

    /**
     * 依据IfieldName查询信息
     *
     * @param selection  条件 "条件1=? and 条件2=?"
     * @param fieldValue new String[]{"条件1的值，条件2的值"}
     * @return
     */
    public T queryByField(String selection, String[] fieldValue) {
        T t = null;
        SQLiteDatabase db = getDatabase();
        Cursor c = db.query(TABLE_NAME, null, selection, fieldValue,
                null, null, null);
        if (c != null && c.getCount() > 0 && c.moveToFirst()) {
            t = getEntity(c);
            c.close();
        }
        return t;
    }

    public T queryByField(String selection, String[] fieldValue, SQLiteDatabase db) {
        T t = null;
        Cursor c = db.query(TABLE_NAME, null, selection, fieldValue,
                null, null, null);
        if (c != null && c.getCount() > 0 && c.moveToFirst()) {
            t = getEntity(c);
            c.close();
        }
        return t;
    }

    /**
     * 封装到实体
     *
     * @param c
     * @return
     */
    public T getEntity(Cursor c) {
        return null;
    }
}
