package com.example.passagewell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.passagewell.entity.User;
import com.example.passagewell.dao.UserDao;
import com.example.passagewell.util.DBAdapter;


public class ZhuceActivity extends AppCompatActivity {
    private static String[] grade=new String[] { "小学", "初中", "高中" };
    private Spinner gradeSpinner;
    private Button zhuce_finish;
    private EditText account;
    private EditText username;
    private EditText passwd;
    private UserDao userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        zhuce_finish=findViewById(R.id.zhucefinish_button);
        gradeSpinner = findViewById(R.id.grade);
        account=findViewById(R.id.user_account);
        username=findViewById(R.id.user_input);
        passwd=findViewById(R.id.password_input);
        //Bundle data2=getIntent().getExtras();

        //DBAdapter db=(DBAdapter) data2.getSerializable("myDBA");
        userdao=new UserDao(this);
        userdao.open();
        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grade); //设置样式与数据
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置浮动下拉的样式
        gradeSpinner.setAdapter(addressAdapter);//为Spinner绑定适配器
        zhuce_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uaccount=account.getText().toString();
                String uname=username.getText().toString();
                String upasswd=passwd.getText().toString();
                String ugrade=gradeSpinner.getSelectedItem().toString();
                User user=new User(uaccount,uname,upasswd,ugrade);
                userdao.addUser(user);
                User rtUser=userdao.getUsers(uaccount,upasswd);
                //user.setId(rtUser.getId());
                //新建意图
                //Intent intent = new Intent(ZhuceActivity.this, MainActivity.class)intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //启动对应的Activity
                //创建Bundle，放置Person对象
                Intent intent=getIntent();
                //创建Bundle，放置Person对象
                Bundle data = new Bundle();
                data.putSerializable("user",rtUser);
                data.putBoolean("isLogin",true);
                //intent携带Bundle
                intent.putExtras(data);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
