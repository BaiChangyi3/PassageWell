package com.example.passagewell.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.passagewell.R;
import com.example.passagewell.TimuService;
import com.example.passagewell.dao.UserDao;
import com.example.passagewell.dao.dealPassage;
import com.example.passagewell.entity.AnswerRange;
import com.example.passagewell.entity.Question;
import com.example.passagewell.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ContentFragment extends Fragment {
    private Button nextBtn;
    Context contextSer;//对应绑定到的Activity
    TextView tv;
    TextView correct;
    private static String TAG = "CONTLIFECYCLE";
    ArrayList<Question> questions;
    Question question;
    private Intent intent;
    //DBAdapter db;
    User user;
    UserDao userdao;
    private TimuService timuService;
    SpannableStringBuilder content;
    //answerList用户填写的答案集合
    private ArrayList<String> answerList;
    //rangeList，空白处长度
    private ArrayList<AnswerRange> ansRangeList=new ArrayList<>();
    //生成ServiceConnection对象，用于Service绑定
    private ServiceConnection conn = new MyConnection();
    public interface Callbacks{
        public void rtMain(User user);
    }
    private Callbacks context;//对应绑定到的实现了该接口的Activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mActivity = (Activity) context;
        this.contextSer=context;

        if (!(context instanceof ListTitleFragment.Callbacks)){
            throw new IllegalStateException("ListTitleFragment所在Activity必须实现Callbacks接口");
        }else{
            this.context = (Callbacks)context;
        }
        //this.context = (ContentFragment.Callbacks)context;
        Log.v(TAG,"1. content_onAttach");
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到题目
        Bundle data = getArguments();
        Vector<String> neirong=(Vector<String>) data.getSerializable("neirong");
        dealPassage dealpassage=new dealPassage(neirong);
        user=(User)data.getSerializable("user");
        //db=(DBAdapter)data.getSerializable("myDBA");
        userdao=new UserDao(contextSer);
        userdao.open();
        //问题数组
        questions= dealpassage.getQuestions();
        //Service显式调用的方式构造intent。
        intent = new Intent(contextSer, TimuService.class);
        //通过Intent绑定指定服务
        //通过conn设置绑定过程中的回调方法
        //第三个参数标示如果Service不存在，则自动生成该Service
        contextSer.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.v(TAG,"2. content_onCreate");

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,container, false);
        nextBtn=view.findViewById(R.id.next_blank);
        tv = (TextView) view.findViewById(R.id.show_content);
        correct=view.findViewById(R.id.correct_content);
        //tv.setText(passage);
        Log.v(TAG,"3. content_onCreateView");
        question=questions.get(0);
        int number=questions.size();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //countService不为null，则绑定成功，调用其add方法
                if(timuService!=null){
                    //按钮为完成，点击后检查答案，按钮文字变更为“下一题”
                    if(nextBtn.getText().equals("完成"))
                    {
                        Question question=questions.get(timuService.getTimuNumber());
                        checkAnswer(question.getTrueAns());
                        if(timuService.getTimuNumber()<number-1)
                        {
                            nextBtn.setText("下一题");
                        }else{
                            nextBtn.setText("返回主页");
                        }
                    } else if(nextBtn.getText().equals("下一题")){ //按钮为下一题，点击后变更题目
                        correct.setText("");
                        timuService.numberIncrease();
                        int timuNum=timuService.getTimuNumber();
                        if(timuNum<number-1)
                        {
                            question=questions.get(timuNum);
                            Blank blank=new Blank(question);
                            answerList.clear();
                            answerList=blank.getAnswerList();
                            ansRangeList.clear();
                            ansRangeList= blank.getAnsRangeList();
                            content=blank.getContent();
                            tv.setText(content);
                            nextBtn.setText("完成");
                        }
                        else{
                            question=questions.get(timuNum);
                            Blank blank=new Blank(question);
                            answerList.clear();
                            answerList=blank.getAnswerList();
                            ansRangeList.clear();
                            ansRangeList= blank.getAnsRangeList();
                            content=blank.getContent();
                            tv.setText(content);
                            nextBtn.setText("完成");
                        }
                    }else{
                        int score=timuService.getScore();
                        int oldScore= user.getScore();
                        int oldSolo=user.getSoloTime();
                        Toast.makeText(contextSer, "本次答题积分+"+score, Toast.LENGTH_SHORT).show();
                        userdao.updateSolo(score+oldScore,oldSolo+1, user.getId());
                        User upUser=userdao.getUsers(user.getAccount(), user.getPasswd());
                        context.rtMain(upUser);
                    }
                }else{
                    Toast.makeText(contextSer, "服务未绑定", Toast.LENGTH_SHORT).show();
                }

            }
        });


        Blank blank=new Blank(question);
        ansRangeList=blank.getAnsRangeList();
        answerList= blank.getAnswerList();
        content=blank.getContent();
        tv.setText(content);
        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }
    //检查答案并且加积分
    void checkAnswer(ArrayList<String> trueAns){
        String[] corr=new String[trueAns.size()];
        boolean check=true;
        String text="";
        for(int i=0;i< answerList.size();i++)
        {
            if(answerList.get(i).equals(trueAns.get(i))){
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#120000"));
                content.setSpan(colorSpan,ansRangeList.get(i).getStart(),ansRangeList.get(i).getEnd(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                timuService.setScore();
                corr[i]="第"+(i+1)+"空回答正确\n";

            }else{
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#db5860"));
                content.setSpan(colorSpan,ansRangeList.get(i).getStart(),ansRangeList.get(i).getEnd(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                corr[i]="第"+(i+1)+"空回答错误，正确答案为："+trueAns.get(i)+"\n";
                check=false;
            }
            text+=corr[i];
        }
        tv.setText(content);
        if(check){
            correct.setText("答案正确");
        }else{
            correct.setText(text);
        }

    }
    //Blank为包含空白的题目类
    class Blank {
        //answerList用户填写的答案集合
        private ArrayList<String> answerList;
        //rangeList，空白处长度
        private ArrayList<AnswerRange> ansRangeList=new ArrayList<>();
        //content用于填充TextView内容，可以点击
        private SpannableStringBuilder content;
        // 答案填空处ClickableSpan对象集合
        private List<BlankClickableSpan> blankClickableSpanList;

        private String originContent="";
        private ArrayList<String> timu;  //一个句子的题目集
        private ArrayList<String> trueAns;  //一个句子的答案集
        private ArrayList<Integer> blankLen=new ArrayList<>();  //一个空白的长度


        public Blank(Question question)
        {
            this.timu=question.getTimu();
            this.blankLen= question.getBlankLen();
            int j=0;
            for(int i=0;i<timu.size();i++)
            {
                if(timu.get(i).equals(""))  //空白处用下划线填充
                {
                    AnswerRange ansRange=new AnswerRange(originContent.length(),originContent.length()+blankLen.get(j));
                    ansRangeList.add(ansRange);
                    for(int k=0;k<blankLen.get(j);k++)
                        originContent+= "_";
                    j++;
                }
                else {
                    originContent+= timu.get(i);
                }
            }
            setData(this.originContent,this.ansRangeList);
        }
        //setData方法设置可编辑的content
        public void setData(String originContent, List<AnswerRange> answerRangeList) {
            if (TextUtils.isEmpty(originContent) || answerRangeList == null
                    || answerRangeList.isEmpty()) {
                return;
            }
            // 获取课文内容
            content = new SpannableStringBuilder(originContent);

            // 设置下划线颜色
            for (AnswerRange range : answerRangeList) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4DB6AC"));
                content.setSpan(colorSpan, range.getStart(), range.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // 答案集合
            answerList = new ArrayList<>();
            for (int i = 0; i < ansRangeList.size(); i++) {
                answerList.add("");
            }

            // 设置填空处点击事件
            blankClickableSpanList = new ArrayList<>();
            for (int i = 0; i < answerRangeList.size(); i++) {
                AnswerRange range = ansRangeList.get(i);
                BlankClickableSpan blankClickableSpan = new BlankClickableSpan(i);
                content.setSpan(blankClickableSpan, range.getStart(), range.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                blankClickableSpanList.add(blankClickableSpan);
            }

            // 设置此方法后，点击事件才能生效
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            //tvContent.setText(content);
        }
        /**
         * 为每个空白设置点击事件
         */
        class BlankClickableSpan extends ClickableSpan {
            private int position;   //position为点击事件在点击事件list中的下标
            public BlankClickableSpan(int position) {
                this.position = position;
            }
            @Override
            public void onClick(final View widget) {
                View view = LayoutInflater.from(contextSer).inflate(R.layout.layout_input, null);
                final EditText etInput = (EditText) view.findViewById(R.id.et_answer);
                Button btnFillBlank = (Button) view.findViewById(R.id.btn_fill_blank);
                // 显示原有答案
                String oldAnswer = answerList.get(position);
                if (!TextUtils.isEmpty(oldAnswer)) {
                    etInput.setText(oldAnswer);
                    etInput.setSelection(oldAnswer.length());
                }
                final PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, dp2px(40));
                // 获取焦点
                popupWindow.setFocusable(true);
                // 为了防止弹出菜单获取焦点之后，点击Activity的其他组件没有响应
                popupWindow.setBackgroundDrawable(new PaintDrawable());
                // 设置PopupWindow在软键盘的上方
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                // 弹出PopupWindow
                popupWindow.showAtLocation(tv, Gravity.BOTTOM, 0, 0);
                btnFillBlank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 填写答案
                        String answer = etInput.getText().toString();
                        fillAnswer(answer, position);
                        popupWindow.dismiss();
                    }
                });
                // 显示软键盘
                InputMethodManager inputMethodManager =
                        (InputMethodManager) contextSer.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                // 不显示下划线
                ds.setUnderlineText(false);
            }
        }
        /**
         * 填写答案
         *
         * @param answer   当前填空处答案
         * @param position 填空位置
         */
        private void fillAnswer(String answer, int position) {
            answer = " " + answer + " ";

            // 移除原来的点击事件
            content.removeSpan(blankClickableSpanList.get(position));

            // 替换答案,range为原来的答案
            AnswerRange range = ansRangeList.get(position);
            content.replace(range.getStart(), range.getEnd(), answer);

            // 更新当前的答案范围
            AnswerRange currentRange = new AnswerRange(range.getStart(),
                    range.getStart() + answer.length());

            ansRangeList.set(position, currentRange);

            // 重新设置点击事件
            content.setSpan(new BlankClickableSpan(position), currentRange.getStart(), currentRange.getEnd(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 答案设置下划线
            content.setSpan(new UnderlineSpan(),
                    currentRange.getStart(), currentRange.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 将答案添加到集合中
            answerList.set(position, answer.replace(" ", ""));

            // 更新内容
            tv.setText(content);

            for (int i = 0; i < ansRangeList.size(); i++) {
                if (i > position) {
                    // 获取下一个答案原来的范围
                    AnswerRange oldNextRange = ansRangeList.get(i);
                    int oldNextAmount = oldNextRange.getEnd() - oldNextRange.getStart();
                    // 计算新旧答案字数的差值
                    int difference = currentRange.getEnd() - range.getEnd();

                    // 移除原来的点击事件
                    content.removeSpan(blankClickableSpanList.get(i));

                    // 更新下一个答案的范围
                    AnswerRange nextRange = new AnswerRange(oldNextRange.getStart() + difference,
                            oldNextRange.getStart() + difference + oldNextAmount);
                    ansRangeList.set(i, nextRange);

                    // 重新设置点击事件
                    content.setSpan(new BlankClickableSpan(i), nextRange.getStart(), nextRange.getEnd(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (!TextUtils.isEmpty(answerList.get(i))) {
                        // 答案设置下划线
                        content.setSpan(new UnderlineSpan(),
                                nextRange.getStart(), nextRange.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            // 更新内容
            tv.setText(content);
        }
        /**
         * dp转px
         *
         * @param dp dp值
         * @return px值
         */
        private int dp2px(float dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    getResources().getDisplayMetrics());
        }
        public SpannableStringBuilder getContent()
        {
            return content;
        }
        public ArrayList<String> getAnswerList(){return answerList;}
        public ArrayList<AnswerRange> getAnsRangeList(){return  ansRangeList; }
    }
    class MyConnection implements ServiceConnection {
        /***
         * 服务绑定时该方法将被调用。通过binder对象获得Service对象。
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder){
            Log.v("MyConnection", "onServiceConnected方法被调用");
            timuService = ((TimuService.MyBind)binder).getService();
        }

        /***
         * 绑定非正常解除时该方法将被调用，如Service服务被意外销毁。
         * 将Service对象置为空
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v("MyConnection", "onServiceDisconnected方法被调用");
            timuService = null;
        }
    }

}
