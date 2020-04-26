package com.xp.zjd.po;

import com.esri.core.map.Graphic;
import com.google.gson.annotations.Expose;
import com.xp.common.tools.Photo;
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
    private Long id;
    @Expose
    private boolean isSelectNative;
    @Expose
    private String ZDNUM;
    @Expose
    private String QUANLI;
    @Expose
    private String bz;
    @Expose
    private List<Photo> photos;
    @Expose
    private User user;

    /**
     * 地块是否已经上传, 创建地块时默认时false
     */
    @Expose
    private Boolean isUpload;
    @Expose
    private List<ZJDGeometry> zjdGeometry;//考虑多部件情况


    /**
     * 删除
     */
    private  List<Graphic> graphics;


    public List<Graphic> getGraphics() {
        if(graphics == null){
            graphics = new ArrayList<>();
        }
        return graphics;
    }

    public void setGraphics(List<Graphic> graphics) {
        this.graphics = graphics;
    }

    public ZJD(String ZDNUM, String QUANLI) {
        this.ZDNUM = ZDNUM;
        this.QUANLI = QUANLI;

        //类初始化
        this.isUpload =false;
        this.zjdGeometry = new ArrayList<>();
    }

    public ZJD() {
        //类初始化
        this.isUpload =false;
        this.zjdGeometry = new ArrayList<>();
    }

    public List<ZJDGeometry> getZjdGeometry() {
        return zjdGeometry;
    }

    public void setZjdGeometry(List<ZJDGeometry> zjdGeometry) {
        this.zjdGeometry = zjdGeometry;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getUpload() {
        return isUpload;
    }

    public void setUpload(Boolean upload) {
        isUpload = upload;
    }

    public Long getId() {
        return id;
    }

    public boolean isSelectNative() {
        return isSelectNative;
    }

    public void setSelectNative(boolean selectNative) {
        isSelectNative = selectNative;
    }

    public void setId(Long id) {
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
        if(QUANLI == null){
            QUANLI = "";
        }
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
        return "ZJD{" +
                "id=" + id +
                ", isSelectNative=" + isSelectNative +
                ", ZDNUM='" + ZDNUM + '\'' +
                ", QUANLI='" + QUANLI + '\'' +
                ", bz='" + bz + '\'' +
                ", photos=" + photos +
                ", user=" + user +
                ", isUpload=" + isUpload +
                '}';
    }
}
