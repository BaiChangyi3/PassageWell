package com.example.passagewell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passagewell.entity.User;

public class IndivadualActivity extends AppCompatActivity {
    //user为用户信息
    private User user;
    private ImageView rtuHome;  //rtuHome返回主页按钮
    private ImageButton myInfo;  //myInfo个人信息（账号、密码等）
    private ImageView myStudyInfo;  //mStudyInfo学习数据
    private TextView myScore;  //myScore积分
    private TextView myPsgNum;  //myPsgNum已学文章数目
    private TextView soloNum;  //soloNum为个人solo次数
    private TextView pkNum;  //pkNumPK次数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indivadual);
        Intent intent=getIntent();
        Bundle data=intent.getExtras();
        user=(User)data.getSerializable("user");
        rtuHome=findViewById(R.id.returnHome);
        myInfo=findViewById(R.id.myinfoButton);
        myStudyInfo=findViewById(R.id.studyInfo);
        myScore=findViewById(R.id.score);
        myPsgNum=findViewById(R.id.passage_number);
        soloNum=findViewById(R.id.soloNum);
        pkNum=findViewById(R.id.pkNum);
        rtuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndivadualActivity.this, BaseActivity.class);
                Bundle data=new Bundle();
                data.putSerializable("user",user);
                intent.putExtras(data);
                //启动对应的Activity
                startActivityForResult(intent, 0);
            }
        });
        String score=Integer.toString(user.getScore());
        myScore.setText(score);
        String solo=Integer.toString(user.getSoloTime());
        soloNum.setText(solo);
        String pk=Integer.toString(user.getPkTime());
        pkNum.setText(pk);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

    }
}
