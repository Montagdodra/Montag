package com.example.oss_project.Model;


public class Model {

    private String Image, Name, Address, Tag1, Tag2, Tag3;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public Model(String image, String name, String address, String tag1, String tag2, String tag3) {
        Image = image;
        Name = name;
        Address = address;
        Tag1 = tag1;
        Tag2 = tag2;
        Tag3 = tag3;
    }
}