package com.example.passagewell;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TimuService extends Service {
    private MyBind myBind;//用于与绑定者通信的Binder
    private int timuNumber; //timuNumber用于记录已显示的题目数量
    private int score;
    public class MyBind extends Binder {
        //MyBind对象提供该方法获得CountServce对象本身
        public TimuService getService(){
            return TimuService.this;
        }
    }
    //Service的构造方法，在这里初始化myBind
    public TimuService() {
        Log.v("CountService","构造方法被调用");
        myBind = new MyBind();
        timuNumber=0;
        score=0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("CountService","onBind方法被调用");
        //该方法将被系统调用，并返回IBinder类型对象给服务绑定者
        return myBind;
    }
    public int getTimuNumber()
    {
        return timuNumber;
    }
    public void numberIncrease(){
        this.timuNumber++;
    }
    public void setScore(){this.score++;}
    public int getScore(){return score;}
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("CountService","onCreate方法被调用");
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("CountService","onUnbind方法被调用");
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("CountService","onDestroy方法被调用");
    }
}

