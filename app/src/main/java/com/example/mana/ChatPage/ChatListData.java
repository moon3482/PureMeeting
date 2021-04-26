package com.example.mana.ChatPage;

public class ChatListData {
    String roomnum;
    String name;
    String profileimg;
    String id;
    String lastMessage;
    int newMessage;

    public int getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ChatListData(String roomnum, String name, String profileimg, String id, String lastMessage) {
        this.roomnum = roomnum;
        this.name = name;
        this.profileimg = profileimg;
        this.id = id;
        this.lastMessage = lastMessage;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(String roomnum) {
        this.roomnum = roomnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }
}
