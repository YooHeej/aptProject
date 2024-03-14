package com.example.aptProject.entity;

public class MyRegion {
    private int mid;
    private String uid;
    private int lCode;

    @Override
    public String toString() {
        return "MyRegion{" +
                "mid=" + mid +
                ", uid='" + uid + '\'' +
                ", lCode=" + lCode +
                '}';
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getlCode() {
        return lCode;
    }

    public void setlCode(int lCode) {
        this.lCode = lCode;
    }

    public MyRegion() {
    }

    public MyRegion(int mid, String uid, int lCode) {
        this.mid = mid;
        this.uid = uid;
        this.lCode = lCode;
    }
}
