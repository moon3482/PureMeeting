package com.example.mana.chatingList;

public class ChatingListClass {
    String name, chatnum, date, text, img;

    public ChatingListClass(String name, String chatnum, String date, String text, String img) {
        this.name = name;
        this.chatnum = chatnum;
        this.date = date;
        this.text = text;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatnum() {
        return chatnum;
    }

    public void setChatnum(String chatnum) {
        this.chatnum = chatnum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
