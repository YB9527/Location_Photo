package com.xp.zjd.po;

import com.google.gson.annotations.Expose;
import com.xp.common.Photo;
import com.xp.usermanager.po.User;

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
    private String ZDNUM;
    @Expose
    private String QUANLI;
    @Expose
    private List<Photo> photos;
    @Expose
    private User user;

    public ZJD(String ZDNUM, String QUANLI) {
        this.ZDNUM = ZDNUM;
        this.QUANLI = QUANLI;
    }

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

    public String getZDNUM() {
        if(ZDNUM == null){
            ZDNUM = "";
        }
        return ZDNUM;
    }

    public void setZDNUM(String ZDNUM) {
        this.ZDNUM = ZDNUM;
    }

    public String getQUANLI() {
        return QUANLI;
    }

    public void setQUANLI(String QUANLI) {
        this.QUANLI = QUANLI;
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
            return (getZDNUM().equals(zjd.getZDNUM()));
        }
        return super.equals(obj);
    }

    public int hashCode() {//hashCode主要是用来提高hash系统的查询效率。当hashCode中不进行任何操作时，可以直接让其返回 一常数，或者不进行重写。
        int result = getZDNUM().hashCode();
        result = 29 * result + getZDNUM().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", isSelectNative=" + isSelectNative +
                ", ZDNUM='" + ZDNUM + '\'' +
                ", QUANLI='" + QUANLI + '\'' +
                '}';
    }
}
