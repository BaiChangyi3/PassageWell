package com.example.passagewell.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

public class DBAdapter implements Serializable {


    private SQLiteDatabase db; //该对象可理解为数据库的连接
    private final Context context;
    private  MyDBOpenHelper dbOpenHelper;
    public DBAdapter(Context context) {
        this.context = context;
    }
    public void open() throws SQLiteException {//该方法用于打开连接
        dbOpenHelper = new MyDBOpenHelper(context);
        try {
            db = dbOpenHelper.openDatabase();
            Log.v("DATABASE","数据库打开");
        } catch (SQLException ex) {
            //db = dbOpenHelper.getReadableDatabase();
            //db=new SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION).getReadableDatabase();
            Log.v("DATABASE","数据库打开失败");
        }
    }
    public void close() {//该方法用于关闭数据库连接
        if (db != null){
            db.close();
            db = null;
        }
    }
    public long insert(String table, ContentValues values)
    {
        return db.insert(table, null, values);
    }
    public long update(String table,ContentValues values,String whereClause)
    {
        return db.update(table,values,whereClause,null);
    }
    public Cursor query(String sql)
    {
        return db.rawQuery(sql,null);
        //return db.query(table,new String[] { KEY_ID, }, ,selc,null,null,null);
    }
}
