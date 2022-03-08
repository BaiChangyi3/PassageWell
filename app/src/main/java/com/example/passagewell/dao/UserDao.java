package com.example.passagewell.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.passagewell.entity.User;
import com.example.passagewell.util.DBAdapter;
public class UserDao {
    private User user;
    private DBAdapter db;
    private final Context context;
    public UserDao(Context context) {
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
    public void addUser(User user)
    {
        ContentValues newValues = new ContentValues();
        newValues.put("account",user.getAccount());
        newValues.put("name",user.getName());
        newValues.put("passwd",user.getPasswd());
        newValues.put("grade",user.getGrade());
        db.insert("userinfo",newValues);
    }
    public void updateScore(int score,int id)
    {
        ContentValues updateValues = new ContentValues();
        updateValues.put("score",score);
        String whereClause="_id="+id;
        db.update("userinfo",updateValues,whereClause);
    }
    public void updateSolo(int score,int solo,int id)
    {
        ContentValues updateValues = new ContentValues();
        updateValues.put("score",score);
        updateValues.put("soloTime",solo);
        String whereClause="_id="+id;
        db.update("userinfo",updateValues,whereClause);
    }
    public void updateGrade(String grade,long id)
    {
        ContentValues updateValues = new ContentValues();
        updateValues.put("grade",grade);
        String whereClause="_id="+id;
        db.update("userinfo",updateValues,whereClause);
    }
    public User getUsers(String account,String passwd)
    {
        String sql="select * from userinfo where account="+account+" and passwd='"+passwd+"'";
        Cursor cursor=db.query(sql);
        if(cursor.getCount()>=1)
        {
            cursor.moveToFirst();
            int id= cursor.getInt(0);
            String username= cursor.getString(2);
            String grade=cursor.getString(4);
            int score=cursor.getInt(5);
            int solo=cursor.getInt(6);
            int pk=cursor.getInt(7);
            user=new User(id,account,username,passwd,grade,score,solo,pk);

        }return user;
    }
}