package com.xp.xzqy.po;

/**
 * Created by Administrator on 2020/3/31.
 */

public class XZDM {
    private int id;
    private String mDJZQDM;
    private String mDJZQMC;
    public XZDM(){

    }
    public XZDM(String mDJZQDM, String mDJZQMC) {
        this.mDJZQDM = mDJZQDM;
        this.mDJZQMC = mDJZQMC;
    }

    public int getId() {
        return id;
    }

    public String getmDJZQDM() {
        return mDJZQDM;
    }

    public String getmDJZQMC() {
        return mDJZQMC;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setmDJZQDM(String mDJZQDM) {
        this.mDJZQDM = mDJZQDM;
    }

    public void setmDJZQMC(String mDJZQMC) {
        this.mDJZQMC = mDJZQMC;
    }

    @Override
    public String toString() {
        return "XZDM{" +
                "id=" + id +
                ", mDJZQDM='" + mDJZQDM + '\'' +
                ", mDJZQMC='" + mDJZQMC + '\'' +
                '}';
    }
}
