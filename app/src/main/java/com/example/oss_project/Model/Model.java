package com.example.oss_project.Model;


public class Model {

    private String Image, Name, Address, Tag1, Tag2, Tag3, Intro, Image1, Lat, Longt;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTag1() {
        return Tag1;
    }

    public void setTag1(String tag1) {
        Tag1 = tag1;
    }

    public String getTag2() {
        return Tag2;
    }

    public void setTag2(String tag2) {
        Tag2 = tag2;
    }

    public String getTag3() {
        return Tag3;
    }

    public void setTag3(String tag3) {
        Tag3 = tag3;
    }

    public String getIntro() {
        return Intro;
    }

    public void setIntro(String intro) {
        Intro = intro;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLongt() {
        return Longt;
    }

    public void setLongt(String longt) {
        Longt = longt;
    }

    public Model(String image ,String name, String image1, String address, String tag1, String tag2, String tag3, String intro, String lat, String longt) {
        Image = image;
        Image1 = image1;
        Name = name;
        Address = address;
        Tag1 = tag1;
        Tag2 = tag2;
        Tag3 = tag3;
        Intro = intro;
        Lat = lat;
        Longt = longt;
    }
}