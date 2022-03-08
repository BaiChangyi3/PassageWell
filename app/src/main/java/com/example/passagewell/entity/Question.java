package com.example.passagewell.entity;

import java.util.ArrayList;

public class Question {
    private ArrayList<String> timu;  //一个句子的题目集
    private ArrayList<String> trueAns;  //一个句子的答案集
    private ArrayList<Integer> blankLen;  //一个空白的长度
    public Question(ArrayList<String> timu, ArrayList<String> trueAns, ArrayList<Integer> blankLen)
    {
        this.timu=timu;
        this.blankLen=blankLen;
        this.trueAns=trueAns;
    }
    public ArrayList<String> getTimu()
    {
        return this.timu;
    }

    public ArrayList<String> getTrueAns() {
        return trueAns;
    }

    public ArrayList<Integer> getBlankLen() {
        return blankLen;
    }
}
