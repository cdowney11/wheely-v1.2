package com.alkliv.wheely.vo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/12/13
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Review {
    private List<Aspect> Aspects;
    private String Author_Name;
    private String Author_Url;
    private String Text;
    private String Time;

    public List<Aspect> getAspects() {
        return Aspects;
    }

    public void setAspects(List<Aspect> aspects) {
        Aspects = aspects;
    }

    public String getAuthor_Name() {
        return Author_Name;
    }

    public void setAuthor_Name(String author_Name) {
        Author_Name = author_Name;
    }

    public String getAuthor_Url() {
        return Author_Url;
    }

    public void setAuthor_Url(String author_Url) {
        Author_Url = author_Url;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
