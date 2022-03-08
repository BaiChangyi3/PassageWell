package com.example.passagewell.dao;
import android.content.Context;
import android.database.Cursor;

import com.example.passagewell.util.*;
import com.example.passagewell.entity.*;

import java.util.Vector;



public class PassageDao {
    private Vector<Passage> passages=new Vector<>();
    private DBAdapter db;
    private final Context context;
    private Vector<String> titles;

    public PassageDao(Context context) {
        this.context = context;
    }

    public void open() {
        db = new DBAdapter(context);
        db.open();
    }

    //该方法用于关闭数据库
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    public Vector<Passage> getPassagesByPhase(String phase)
    {
        passages.clear();
        String sql="select title,author,time,passage from passageinfo where phase= '"+phase+"'";
        Passage passage;

        Cursor cursor=db.query(sql);
        if(cursor==null ||cursor.getCount()==0)
            return null;
        int count = cursor.getCount();
        cursor.moveToFirst();
        for(int i=0;i<count;i++)
        {
            String title=cursor.getString(0);
            String author=cursor.getString(1);
            String time=cursor.getString(2);
            String pass=cursor.getString(3);
            passage=new Passage(title,author,time);
            passage.setPassage(pass);

            passages.addElement(passage);
            cursor.moveToNext();
        }
        return this.passages;
    }
    public boolean searchPassage(String title)
    {
        passages.clear();
        String sql="select title,author,time,passage from passageinfo where title= '"+title+"'";
        Passage passage;

        Cursor cursor=db.query(sql);
        if(cursor==null ||cursor.getCount()==0)
            return false;
        int count = cursor.getCount();
        cursor.moveToFirst();
        for(int i=0;i<count;i++)
        {
            String titles=cursor.getString(0);
            String author=cursor.getString(1);
            String time=cursor.getString(2);
            String pass=cursor.getString(3);
            passage=new Passage(title,author,time);
            passage.setPassage(pass);

            passages.addElement(passage);
            cursor.moveToNext();
        }
        return true;
    }
    public Vector<Passage> getPassagesByTitle(String seaTitle){
        boolean isFind=searchPassage(seaTitle);
        if(isFind){
            return this.passages;
        }
        return this.passages;
    }
}