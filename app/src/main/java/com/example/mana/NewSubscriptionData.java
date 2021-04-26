package com.example.mana;

public class NewSubscriptionData {
    String myId;
    String youId;
    String profileImage;
    String name;

    public NewSubscriptionData(String myId, String youId, String profileImage, String name) {
        this.myId = myId;
        this.youId = youId;
        this.profileImage = profileImage;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getYouId() {
        return youId;
    }

    public void setYouId(String youId) {
        this.youId = youId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
