package com.wjdgus262.ocrtestapplication;

public class select_item {

    String number;
    String location;
    public select_item(String number,String location) {
        this.number = number; this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
