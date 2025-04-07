package com.example.mana.shopInfomation;

public class ShopInfoMenuData {

    String meunname;
    String imagepath;
    String price;
    String explanation;

    public ShopInfoMenuData(String meunname, String imagepath, String price, String explanation) {
        this.meunname = meunname;
        this.imagepath = imagepath;
        this.price = price;
        this.explanation = explanation;
    }



    public String getMeunname() {
        return meunname;
    }

    public void setMeunname(String meunname) {
        this.meunname = meunname;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
