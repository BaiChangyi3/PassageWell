package com.example.passagewell.entity;

public class AnswerRange {
    private int start;
    private int end;
    public AnswerRange(int start, int end)
    {
        this.start=start;
        this.end=end;
    }
    public int getStart()
    {
        return this.start;
    }
    public int getEnd()
    {
        return this.end;
    }
}
