package com.example.passagewell.entity;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String account;
    private String username;
    private String passwd;
    private String grade;
    private int score;
    private int pkTime;
    private int soloTime;

    //此构造方法用于从数据库中得到用户信息
    public User(int id,String account,String username,String passwd,String grade,int score,int solo,int pk)
    {
        this.account=account;
        this.username=username;
        this.passwd=passwd;
        this.grade=grade;
        this.id=id;
        this.score=score;
        this.pkTime=pk;
        this.soloTime=solo;
    }
    //此构造方法用于注册时生成用户
    public User(String account,String username,String passwd,String grade)
    {
        this.account=account;
        this.username=username;
        this.passwd=passwd;
        this.grade=grade;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public void setScore(int score)
    {
        this.score=score;
    }
    public void setSoloTime(int times){this.soloTime=times;}
    public void setPkTime(int times){this.pkTime=times;}
    public int getId()
    {
        return this.id;
    }
    public String getAccount()
    {
        return this.account;
    }
    public String getName()
    {
        return this.username;
    }
    public String getPasswd()
    {
        return this.passwd;
    }
    public String getGrade()
    {
        return this.grade;
    }
    public int getScore()
    {
        return this.score;
    }
    public int getPkTime(){return this.pkTime;}
    public int getSoloTime(){return this.soloTime;}

}
