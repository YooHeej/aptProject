package com.example.aptProject.entity;

public class LocationCode {
    private int lCode;
    private String lName;

    @Override
    public String toString() {
        return "LocationCode{" +
                "lCode=" + lCode +
                ", lName='" + lName + '\'' +
                '}';
    }

    public int getlCode() {
        return lCode;
    }

    public void setlCode(int lCode) {
        this.lCode = lCode;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public LocationCode() {
    }

    public LocationCode(int lCode, String lName) {
        this.lCode = lCode;
        this.lName = lName;
    }
}
