package com.jagtapazad.ivymrkevt.HelperClasses;

public class NoticeClass {

    String notice;
    long number;
    String imgURL;

    public NoticeClass() {

    }

    @Override
    public String toString() {
        return "NoticeClass{" +
                "notice='" + notice + '\'' +
                ", number=" + number +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }

    public NoticeClass(String notice, long number, String imgURL) {
        this.notice = notice;
        this.number = number;
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
