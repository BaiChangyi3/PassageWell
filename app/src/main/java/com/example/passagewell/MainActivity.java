package com.example.passagewell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passagewell.dao.UserDao;
import com.example.passagewell.entity.User;
import com.example.passagewell.util.DBAdapter;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String TAG="LIFECYCLE";
    private User user;
    private Button soloBtn;
    private Button pkBtn;
    private boolean isLogin=false;
    Context context;
    //DBAdapter db=new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.tool_bar);
        soloBtn=findViewById(R.id.solo_button);
        pkBtn=findViewById(R.id.pk_button);
        setSupportActionBar(toolbar);
        context=getApplicationContext();
        //db.open();
        soloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin)
                {
                    Intent soloIntent=new Intent(MainActivity.this, SoloActivity.class);
                    Bundle data=new Bundle();
                    data.putSerializable("user",user);
                    //data.putSerializable("myDBA",db);
                    soloIntent.putExtras(data);
                    startActivityForResult(soloIntent, 3);
                }
                else{
                    Toast.makeText(context, "还没有登录哦", Toast.LENGTH_SHORT).show();
                }
            }
            });
        pkBtn.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                Toast.makeText(context, "此功能暂未开通哦", Toast.LENGTH_SHORT).show();
            }
        });
        Log.v(TAG,"1. onCreate");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //菜单文件来自于res/menu/main.xml
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_menu_indivadual:
                if(user!=null){
                    Intent intent3 = new Intent(MainActivity.this, IndivadualActivity.class);
                    Bundle data=new Bundle();
                    data.putSerializable("user",user);
                    intent3.putExtras(data);
                    //启动对应的Activity
                    startActivityForResult(intent3, 2);
                }else{
                    Toast.makeText(context, "还没有登录哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.main_menu_denglu:
                Intent intent2 = new Intent(MainActivity.this, DengluActivity.class);
                Bundle data1=new Bundle();
                //data1.putSerializable("myDBA",db);
                intent2.putExtras(data1);
                //启动对应的Activity
                startActivityForResult(intent2, 0);
                break;
            case R.id.main_menu_zhuce:
                Intent intent = new Intent(MainActivity.this, ZhuceActivity.class);
                Bundle data2=new Bundle();
                //data2.putSerializable("myDBA",db);
                intent.putExtras(data2);
                //启动对应的Activity
                startActivityForResult(intent, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"2. onStart");
    }

    /***
     * 恢复Activity，该方法将被调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"3. onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"4. onPause");
    }

    /***
     * 停止Activity，该方法将被调用
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"5. onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //db.close();
        Log.v(TAG,"6. onDestroy ");
    }

    protected void onRestart() {
        super.onRestart();
        Log.v(TAG,"7. onRestart");
    }
    /***
     * 被调Activity一旦返回值,该回调方法将被系统自动调用
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //如果请求码是0且返回的结果码为Activity.RESULT_OK
        //请求的Activity也可以返回多种结果，如RESULT_CANCELED
        if(resultCode== Activity.RESULT_OK){
            //取出数据
            Bundle data=intent.getExtras();
            user=(User)data.getSerializable("user");
            if(requestCode==0||requestCode==1){
                isLogin=data.getBoolean("isLogin");
            }else if(requestCode==3){
                user=(User)data.getSerializable("upUser");
            }
        }
    }
}