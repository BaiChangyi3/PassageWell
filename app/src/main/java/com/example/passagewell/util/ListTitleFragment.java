package com.example.passagewell.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.passagewell.R;
import com.example.passagewell.entity.Passage;

import java.util.ArrayList;
import java.util.Vector;

public class ListTitleFragment extends Fragment {
    private String[] titles;
    private Vector<Passage> passages;
    private Activity mActivity;
    private ListView listView;
    private static String TAG = "LISTLIFECYCLE";
    View  nowView;
    ArrayList<String>listData=new ArrayList<>();
    MyListAdapter myListAdapter;

    //定义了一个接口Callbacks
    //使用ListTitleFragment的Activity需要实现该接口
    //ListTitleFragment将使用该接口与Activity进行通信
    public interface Callbacks{
        public void onItemSelected(int id);
    }
    private Callbacks context;//对应绑定到的Activity

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof  Callbacks)){
            throw new IllegalStateException("ListTitleFragment所在Activity必须实现Callbacks接口");
        }else{
            this.context = (Callbacks)context;
        }

        //mActivity = (Activity) context;
        Bundle data = getArguments();
        passages = (Vector<Passage>) data.getSerializable("passages");
        int titleNumber = passages.size();
        titles = new String[titleNumber];
        for (int i = 0; i < titleNumber; i++)
            titles[i] = passages.get(i).getTitle();
        Log.v(TAG,"1. list_onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo();
        Log.v(TAG,"2. list_onCreate");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        nowView = inflater.inflate(R.layout.fragment_titles,container,false);
        listView=(ListView)nowView.findViewById(R.id.list);
        myListAdapter=new MyListAdapter();
        myListAdapter.notifyDataSetChanged();
        // 设置适配器
        listView.setAdapter(myListAdapter);
        Log.v(TAG,"3. list_onCreateView");
        return nowView;
    }
    private void demo()
    {
        listData.clear();
        for(int i=0;i< titles.length;i++)
            listData.add(titles[i]);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TextView mFragment2_tv = (TextView) getActivity().findViewById(R.id.show_content);//获取其它fragment中的控件引用的唯一方法!!!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String selec_title=listData.get(position);
                context.onItemSelected(position);
            }
        });
        Log.v(TAG,"4. onActivityCreated");
    }
    public class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            if(converView==null)
            {
                converView=getLayoutInflater().inflate(R.layout.item,parent,false);
            }
            String c= (String)getItem(position);
            TextView textView=(TextView)converView.findViewById(R.id.title_text);
            textView.setText(c);
            return converView;
        }
    }

}

