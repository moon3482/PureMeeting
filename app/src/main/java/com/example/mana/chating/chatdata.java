package com.example.mana.chating;

public class chatdata {
    String type;
    String name;
    String msg;
    String img;
    String id;
    String room;
    String youid;


    public chatdata(String type, String name, String msg, String img, String id, String room,String youid) {
        this.type = type;
        this.name = name;
        this.msg = msg;
        this.img = img;
        this.id = id;
        this.room = room;
        this.youid =youid;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getYouid() {
        return youid;
    }

    public void setYouid(String youid) {
        this.youid = youid;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public String getImg() {
        return img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
