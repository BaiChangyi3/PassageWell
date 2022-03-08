package com.example.passagewell.entity;

import java.util.Vector;
import java.io.Serializable;


public class Passage implements Serializable{
    private int id;
    private String title;
    private String author;
    private Vector<String> passage=new Vector<>();
    private String phase;
    private String time;

    public Passage  (String title,String author,String time)
    {
        this.title=title;
        this.author=author;
        this.time=time;
    }
    public void setPassage(String passage)
    {
        int info=0;
        char ch=passage.charAt(info);
        String aline="";
        while(info<passage.length()){

            ch=passage.charAt(info);
            //以。？！划分为一句放在vector里

            if(ch!='。'&&ch!='？'&&ch!='！')
            {
                aline+=ch;
                info++;
            }
            else{
                aline+=ch;
                info++;
                if(info<=passage.length()-1)
                {
                    char ch_next=passage.charAt(info);
                    if(ch_next=='"')
                    {
                        aline+=ch_next;
                        info++;
                    }
                    if(ch_next=='’')
                    {
                        aline+=ch_next;
                        info++;
                        if(info<=passage.length()-1)
                        {
                            char ch_next1=passage.charAt(info);
                            if(ch_next=='"')
                            {
                                aline+=ch_next1;
                                info++;
                            }
                        }

                    }
                    String oneline=new String(aline);
                    this.passage.add(oneline);
                    aline="";
                }else {
                    String oneline=new String(aline);
                    this.passage.add(oneline);
                    aline="";
                }
            }
        }
    }
    public String getTitle()
    {
        return this.title;
    }
    public String getAuthor()
    {
        return this.author;
    }
    public String getTime()
    {
        return this.time;
    }
    public Vector<String> getPassage()
    {
        return this.passage;
    }
}