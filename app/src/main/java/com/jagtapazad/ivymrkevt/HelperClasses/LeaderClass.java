package com.jagtapazad.ivymrkevt.HelperClasses;



public class LeaderClass {
    String fname;
    long score;

    public LeaderClass() {

    }

    @Override
    public String toString() {
        return "LeaderClass{" +
                "fname='" + fname + '\'' +
                ", score=" + score +
                '}';
    }

    public LeaderClass(String fname, long score) {
        this.fname = fname;
        this.score = score;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
