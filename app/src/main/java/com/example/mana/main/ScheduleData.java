package com.example.mana.main;

public class ScheduleData {
    String id;
    String shopcode;
    String shopname;
    String shoptype;
    String shopaddress;
    String shopstarttime;
    String endtime;
    String roomnum;
    String reservetime;
    String index;
    String name;
    String profilethumimg;
    String dday;
    public ScheduleData(String id, String shopcode, String shopname, String shoptype, String shopaddress, String shopstarttime, String endtime, String roomnum, String reservetime, String index, String name, String profilethumimg, String dday) {
        this.id = id;
        this.shopcode = shopcode;
        this.shopname = shopname;
        this.shoptype = shoptype;
        this.shopaddress = shopaddress;
        this.shopstarttime = shopstarttime;
        this.endtime = endtime;
        this.roomnum = roomnum;
        this.reservetime = reservetime;
        this.index = index;
        this.name = name;
        this.profilethumimg = profilethumimg;
        this.dday = dday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopcode() {
        return shopcode;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    public String getShopstarttime() {
        return shopstarttime;
    }

    public void setShopstarttime(String shopstarttime) {
        this.shopstarttime = shopstarttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(String roomnum) {
        this.roomnum = roomnum;
    }

    public String getReservetime() {
        return reservetime;
    }

    public void setReservetime(String reservetime) {
        this.reservetime = reservetime;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilethumimg() {
        return profilethumimg;
    }

    public void setProfilethumimg(String profilethumimg) {
        this.profilethumimg = profilethumimg;
    }

    public String getDday() {
        return dday;
    }

    public void setDday(String dday) {
        this.dday = dday;
    }
}
