package com.example.alcoholsafe;

public class Recycler_item {

    String title;

    String content;

    public Recycler_item(String title, String content) {

        this.title = title;
        this.content = content;
    }

    public String getTitle(){

        return title;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public String getContent(){

        return content;
    }

    public void setContent(String content){
        this.content = content;
    }


}
