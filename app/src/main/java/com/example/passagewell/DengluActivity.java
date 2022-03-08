package com.example.passagewell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passagewell.dao.UserDao;
import com.example.passagewell.entity.User;
import com.example.passagewell.util.DBAdapter;

public class DengluActivity extends AppCompatActivity {
    private Button denglu;//下一步按键
    private EditText userPasswE;//密码控件
    private EditText userAccount;
    private User user;
    //private DBAdapter db;
    private UserDao usd;


    private static String TAG = "LIFECYCLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denglu);

        //获得各种控件
        denglu = findViewById(R.id.denglu_button);

        userPasswE = findViewById(R.id.password);
        userAccount=findViewById(R.id.account);
        //Bundle data1=getIntent().getExtras();
        //DBAdapter db=(DBAdapter) data1.getSerializable("myDBA");
        usd=new UserDao(this);
        usd.open();
        //db=new DBAdapter(this);
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_account=userAccount.getText().toString();
                String passwd=userPasswE.getText().toString();
                user=usd.getUsers(user_account,passwd);
                if(user!=null)
                {
                    Intent intent=getIntent();
                    //创建Bundle，放置Person对象
                    Bundle data = new Bundle();
                    data.putSerializable("user",user);
                    data.putBoolean("isLogin",true);
                    //intent携带Bundle
                    intent.putExtras(data);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    /*
                    //新建意图
                    Intent intent = new Intent(DengluActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //启动对应的Activity
                    //创建Bundle，放置Person对象
                    Bundle data = new Bundle();
                    data.putSerializable("user",user);
                    //intent携带Bundle
                    intent.putExtras(data);
                    startActivity(intent);
                    finish();*/
                }
                else{
                    String text="账号或密码错误，请重试";
                    Toast.makeText(DengluActivity.this,text, Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
