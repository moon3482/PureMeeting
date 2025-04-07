package com.example.mana.mainPage;

public class MainLoadDataClass {
    String name;
    String profileImg;
    String age;
    String area;
    String userid;

    public MainLoadDataClass(String name, String profileImg, String age, String area, String userid) {
        this.name = name;
        this.profileImg = profileImg;
        this.age = age;
        this.area = area;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
