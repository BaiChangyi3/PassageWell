package com.example.passagewell;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.passagewell.dao.PassageDao;
import com.example.passagewell.entity.Passage;
import com.example.passagewell.entity.User;
import com.example.passagewell.util.ContentFragment;
import com.example.passagewell.util.DBAdapter;
import com.example.passagewell.util.ListTitleFragment;

import java.util.Vector;

public class SoloActivity extends AppCompatActivity implements ListTitleFragment.Callbacks,ContentFragment.Callbacks{
    private static String[] phase = new String[]{"小学", "初中", "高中"};
    private EditText searchEdit;
    private Button searchBtn;
    private Spinner phaseSpinner;
    private Vector<Passage> passages;  //passages记录当前选择阶段的文章
    private PassageDao psgDao;
    ListTitleFragment lstFrag;
    User user;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo);
        searchBtn=findViewById(R.id.search_button);
        searchEdit=findViewById(R.id.search);
        //View view= this.getLayoutInflater().inflate(R.layout.activity_solo,null);
        phaseSpinner=findViewById(R.id.phase_spin);
        psgDao=new PassageDao(this);
        psgDao.open();
        context=getApplicationContext();
        Intent inten=getIntent();
        Bundle dataUser=inten.getExtras();
        user=(User)dataUser.getSerializable("user");

        // 生成适配器
        ArrayAdapter<String> phaseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phase); //设置样式与数据
        phaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置浮动下拉的样式
        phaseSpinner.setAdapter(phaseAdapter);//为Spinner绑定适配器
        // 为spinner设置选择事件
        phaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {// 如果选项被选择
                String phase=phaseSpinner.getSelectedItem().toString();
                passages=psgDao.getPassagesByPhase(phase);
                Bundle data=new Bundle();
                data.putSerializable("passages",passages);
                lstFrag=new ListTitleFragment();
                lstFrag.setArguments(data);
                //将新生成的Fragment挂在R.id.content标签下
                FragmentManager fr = getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                ft.replace(R.id.list_titles,lstFrag);
                ft.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=searchEdit.getText().toString();
                Vector<Passage> pass=psgDao.getPassagesByTitle(title);
                //数据库中没有该文章
                if(pass.isEmpty()){
                    Toast.makeText(context, "暂时还没有这篇文章哦", Toast.LENGTH_SHORT).show();
                }
                //数据库中有该文章
                else{
                    //生成新的Fragment
                    Fragment cotFrag = new ContentFragment();
                    //生成Bundle，并在其中设置需要传递的数据
                    Bundle data = new Bundle();
                    Passage passage=pass.get(0);
                    Vector<String> neirong=passage.getPassage();
                    data.putSerializable("user",user);
                    data.putSerializable("neirong",neirong);
                    cotFrag.setArguments(data);
                    //将新生成的Fragment挂在R.id.content标签下
                    FragmentManager fr = getSupportFragmentManager();
                    FragmentTransaction ft = fr.beginTransaction();
                    ft.replace(R.id.content,cotFrag);
                    ft.commit();//思考Fragment的onCreate方法执行的时间
                }
            }
        });
    }
    //用户选择题目后调用该方法，显示题目的fragment
    public void onItemSelected(int id){

        //生成新的Fragment
        Fragment cotFrag = new ContentFragment();
        //生成Bundle，并在其中设置需要传递的数据

        Bundle data = new Bundle();
        Passage passage=passages.get(id);
        Vector<String> neirong=passage.getPassage();
        data.putSerializable("user",user);
        data.putSerializable("neirong",neirong);
        cotFrag.setArguments(data);
        //将新生成的Fragment挂在R.id.content标签下
        FragmentManager fr = getSupportFragmentManager();
        FragmentTransaction ft = fr.beginTransaction();
        ft.replace(R.id.content,cotFrag);
        ft.commit();//思考Fragment的onCreate方法执行的时间
    }
    //用户做完题目后调用此方法，返回主页面
    public void rtMain(User user){
        Intent intent=getIntent();
        Bundle data=new Bundle();
        data.putSerializable("upUser",user);
        intent.putExtras(data);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
