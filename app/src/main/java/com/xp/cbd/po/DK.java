package com.xp.cbd.po;

import com.xp.common.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/3/31.
 */

public class DK {
    /**
     * 是否已经查找过本地文件
     */
    private boolean isSelectNative;
    private String mDKBM;
    private String mDKMC;
    private int id;
    private List<Photo> photos;


    public DK() {

    }

    public int getId() {
        return id;
    }

    public boolean isSelectNative() {
        return isSelectNative;
    }

    public void setSelectNative(boolean selectNative) {
        isSelectNative = selectNative;
    }

    public void setId(int id) {
        this.id = id;
    }
    public DK(String mDKBM, String mDKMC) {
        this.mDKBM = mDKBM;
        this.mDKMC = mDKMC;
    }


    public String getmDKBM() {
        return mDKBM;
    }

    public String getmDKMC() {
        return mDKMC;
    }

    public void setmDKBM(String mDKBM) {
        this.mDKBM = mDKBM;
    }

    public void setmDKMC(String mDKMC) {
        this.mDKMC = mDKMC;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "DK{" +
                "mDKBM='" + mDKBM + '\'' +
                ", mDKMC='" + mDKMC + '\'' +
                ", id=" + id +
                '}';
    }
}
