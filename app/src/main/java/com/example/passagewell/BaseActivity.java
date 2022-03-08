package com.example.passagewell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passagewell.entity.User;

public class BaseActivity extends AppCompatActivity {
    private TextView myName;
    private TextView myAccount;
    private TextView myPasswd;
    private TextView myGrade;
    private ImageView rtuLast;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        rtuLast=findViewById(R.id.returnLast);
        myName=findViewById(R.id.myName);
        myAccount=findViewById(R.id.myAccount);
        myPasswd=findViewById(R.id.myPasswd);
        myGrade=findViewById(R.id.myPhase);

        Intent intent=getIntent();
        Bundle data=intent.getExtras();
        user=(User)data.getSerializable("user");

        myName.setText(user.getName());
        myAccount.setText(user.getAccount());
        myPasswd.setText(user.getPasswd());
        myGrade.setText(user.getGrade());

        rtuLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


    }
}
