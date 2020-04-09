package com.xp.zjd.po;

import com.google.gson.annotations.Expose;
import com.xp.common.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/3/31.
 */

public class ZJD {
    /**
     * 是否已经查找过本地文件
     */
    @Expose
    private long id;
    @Expose
    private boolean isSelectNative;
    @Expose
    private String mDKBM;
    @Expose
    private String mDKMC;
    @Expose
    private List<Photo> photos;


    public ZJD() {

    }

    public long getId() {
        return id;
    }

    public boolean isSelectNative() {
        return isSelectNative;
    }

    public void setSelectNative(boolean selectNative) {
        isSelectNative = selectNative;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ZJD(String mDKBM, String mDKMC) {
        this.mDKBM = mDKBM;
        this.mDKMC = mDKMC;
    }


    public String getmDKBM() {
        if (mDKBM == null) {
            return "";
        }
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
        if (photos == null) {
            photos = new ArrayList<>();
        }
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ZJD) {
            ZJD zjd = (ZJD) obj;
            return (getmDKBM().equals(zjd.getmDKBM()));
        }
        return super.equals(obj);
    }

    public int hashCode() {//hashCode主要是用来提高hash系统的查询效率。当hashCode中不进行任何操作时，可以直接让其返回 一常数，或者不进行重写。
        int result = getmDKBM().hashCode();
        result = 29 * result + getmDKBM().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", isSelectNative=" + isSelectNative +
                ", mDKBM='" + mDKBM + '\'' +
                ", mDKMC='" + mDKMC + '\'' +
                '}';
    }
}
