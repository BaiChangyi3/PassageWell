package com.example.passagewell.dao;

import com.example.passagewell.entity.Question;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class dealPassage {
    private String[] statements;  //句子数组
    private Vector<String> originPassage;  //文章
    Random which=new Random();  //随机数用于随机得到句子；


    //问题数组
    ArrayList<Question> questions=new ArrayList<>();

    public dealPassage(Vector<String>originPassage )
    {
        this.originPassage=originPassage;
    }
    //初始化随机生成的句子数组
    public void setStatement()
    {
        int steNumber=originPassage.size();
        if(steNumber>=6)
        {
            int num=(int)Math.sqrt(steNumber);  //num生成的句子数量
            statements=new String[num*2];
            for(int i=0;i<num*2;i++)
            {
                String statement=originPassage.elementAt(which.nextInt(steNumber-1));
                this.statements[i]=statement;
            }
        }
        else{
            statements=new String[steNumber];
            for(int i=0;i<steNumber;i++)
            {
                String statement=originPassage.get(i);
                this.statements[i]=statement;
            }
        }
    }
    //寻找句子中所有标点的下标
    public Vector<Integer> serachMarkIn(String str)
    {
        Vector<Integer> markIn=new Vector<>();
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            //||ch=='。'||ch=='？'||ch=='！'||ch=='：'||ch=='；'||ch=='“'||ch=='、'
            //                    ||ch=='《'||ch=='》'||ch=='‘'||ch=='’'||ch!='”')
            if(ch=='，')
            {
                markIn.add(i);
            }else if(ch=='。'){
                markIn.add(i);
            }else if(ch=='？'){
                markIn.add(i);
            }else if(ch=='：'){
                markIn.add(i);
            }else if(ch=='；'){
                markIn.add(i);
            }else if(ch=='”'){
                markIn.add(i);
            }else if(ch=='“'){
                markIn.add(i);
            }else if(ch=='‘'){
                markIn.add(i);
            }else if(ch=='’'){
                markIn.add(i);
            }else if(ch=='《'){
                markIn.add(i);
            }else if(ch=='》'){
                markIn.add(i);
            }else if(ch=='、'){
                markIn.add(i);
            }
        }
        return markIn;
    }
    //处理带换行符的句子
    public String dealHuanhang(String aline)
    {
        String rtuLine;
        if (aline.indexOf('\r') != -1) {
            rtuLine=aline.substring(2);
        }
        else{
            rtuLine=aline;
        }
        return rtuLine;
    }
    //得到每个句子的空白处
    public void getQuestion(String aline)
    {
        //题目
        ArrayList<String> timu=new ArrayList<>();
        //答案
        ArrayList<String> tureAnswer=new ArrayList<>();
        //空白处长度
        ArrayList<Integer> blankLen=new ArrayList<>();
        timu.clear();
        tureAnswer.clear();
        blankLen.clear();
        String line=this.dealHuanhang(aline);  //line为不包括换行符的句子
        Random ram = new Random();
        Vector<Integer> markIn;
        markIn=serachMarkIn(line);
        int markNum=markIn.size();
        switch(markNum)
        {
            //句子中只有一个标点，空白处为info到标点之间
            case 1:
                int markin=markIn.get(0);  //markin为标点在句子中的下标
                int info=ram.nextInt(markin);  //info为不包括标点的随机数
                timu.add(line.substring(0,info));
                tureAnswer.add(line.substring(info,markin));
                blankLen.add(markin-info);
                timu.add("");
                timu.add(line.substring(markin));
                break;
            //句子中有2个标点,空白处为前一句或者后一句
            case 2:
               int info1=ram.nextInt(markIn.get(1));
               if(info1==markIn.get(0)){   //当随机数为第一个标点下标时再充型生成随机数
                   info1=ram.nextInt(markIn.get(1));
               }
                if(info1>markIn.get(0))  //挖后一句
                {
                    tureAnswer.add(line.substring(markIn.get(0)+1,markIn.get(1)));
                    blankLen.add(markIn.get(1)-markIn.get(0)-1);
                    timu.add(line.substring(0,markIn.get(0)+1));
                    timu.add("");
                    timu.add(line.substring(markIn.get(1)));
                }else if(info1<markIn.get(0)){  //挖前一句
                    tureAnswer.add(line.substring(0,markIn.get(0)));
                    blankLen.add(markIn.get(0)-0);
                    timu.add("");
                    timu.add(line.substring(markIn.get(0)));
                }
                break;
            //句子中有三个标点，空白处为中间那句
            case 3:
                tureAnswer.add(line.substring(markIn.get(0)+1,markIn.get(1)));
                blankLen.add(markIn.get(1)-markIn.get(0)-1);
                timu.add(line.substring(0,markIn.get(0)+1));
                timu.add("");
                timu.add(line.substring(markIn.get(1),markIn.get(2)+1));
                break;
                //有三个及以上的标点，则随机挖两个空白，以标点为界限挖一句
            default:
                int info3=ram.nextInt(markNum);  //info3为要挖的第一个空白句的标点在markIn中的下标
                int info4;     //info4为要挖的第2个空白句的标点在markIn中的下标
                if(info3==0){  //第一处空白为第一个标点之前的句子
                    info4=info3+2;
                    tureAnswer.add(line.substring(0,markIn.get(info3)));  //第一句
                    blankLen.add(markIn.get(info3)-0);
                    tureAnswer.add(line.substring(markIn.get(info4-1)+1,markIn.get(info4)));  //第二句
                    blankLen.add(markIn.get(info4)-markIn.get(info4-1)-1);
                    timu.add("");
                    timu.add(line.substring(markIn.get(info3),markIn.get(info3+1)+1));
                    timu.add("");
                    timu.add(line.substring(markIn.get(info4)));
                }
                else if(info3==(markNum-1))   //第一处空白为最后一句
                {
                    info4=info3-2;
                    tureAnswer.add(line.substring(markIn.get(info4-1)+1,markIn.get(info4)));  //第一句
                    blankLen.add(markIn.get(info4)-markIn.get(info4-1)-1);
                    tureAnswer.add(line.substring(markIn.get(info3-1)+1,markIn.get(info3)));  //第二句
                    blankLen.add(markIn.get(info3)-markIn.get(info3-1)-1);
                    timu.add(line.substring(0,markIn.get(info4-1)+1));
                    timu.add("");
                    timu.add(line.substring(markIn.get(info4),markIn.get(info3-1)+1));
                    timu.add("");
                    timu.add(line.substring(markIn.get(info3)));
                }else{
                    info4=info3+1;
                    tureAnswer.add(line.substring(markIn.get(info3-1)+1,markIn.get(info3)));  //第一句
                    blankLen.add(markIn.get(info3)-markIn.get(info3-1)-1);
                    tureAnswer.add(line.substring(markIn.get(info4-1)+1,markIn.get(info4)));  //第二句
                    blankLen.add(markIn.get(info4)-markIn.get(info4-1)-1);
                    timu.add(line.substring(0,markIn.get(info3-1)+1));
                    timu.add("");
                    timu.add(line.substring(markIn.get(info3),markIn.get(info4-1)+1));
                    timu.add("");
                    timu.add(line.substring(markIn.get(info4)));
                }
                break;
        }
        Question question=new Question(timu,tureAnswer,blankLen);
        questions.add(question);
    }
    //得到所有句子的空白
    public ArrayList<Question> getQuestions() {
        this.setStatement();

        for(int i=0;i<this.statements.length;i++)
        {
            String aline=statements[i];
            this.getQuestion(aline);
        }
        return questions;
    }
}


