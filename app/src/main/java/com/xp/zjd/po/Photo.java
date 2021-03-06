package com.xp.zjd.po;

import com.google.gson.annotations.Expose;
import com.xp.common.tools.DateTool;
import com.xp.common.tools.FileTool;

import java.util.Date;

/**
 * Created by Administrator on 2020/3/31.
 */
public class Photo {
    @Expose
    private String path;
    @Expose
    private String name;
    @Expose
    private String bz;
    @Expose
    private Integer id;
    @Expose
    private Boolean isUpload;

    /**
     * 照片纬度
     */
    @Expose
    private Double latitude;
    /**
     * 照片经度
     */
    @Expose
    private Double longitude;
    /**
     * 拍照时间
     */
    @Expose
    private String createDate;
    private ZJD zjd;

    /**
     * 是否已经上传文件
     */

    public Photo(String path, boolean isUpload) {
        setPath(path);
        this.isUpload = isUpload;
        this.createDate = DateTool.dataFormat(new Date());
    }

    public Photo() {
        this.createDate = DateTool.dataFormat(new Date());
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public ZJD getZjd() {
        return zjd;
    }

    public void setZjd(ZJD zjd) {
        this.zjd = zjd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getUpload() {
        if (isUpload == null) {
            return false;
        }
        return isUpload;
    }

    public void setUpload(Boolean upload) {
        isUpload = upload;
    }

    public void setPath(String path) {

        this.path = path;
        setName(FileTool.getFileName(path));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getPath() {
        if (path == null) {
            return "";
        }
        return path;
    }

    public String getName() {
        return name;
    }

    public String getBz() {
        return bz;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Photo) {
            Photo photo = (Photo) obj;
            return (getPath().equals(photo.getPath()));
        }
        return super.equals(obj);
    }

    public int hashCode() {//hashCode主要是用来提高hash系统的查询效率。当hashCode中不进行任何操作时，可以直接让其返回 一常数，或者不进行重写。
        int result = getPath().hashCode();
        result = 29 * result + getPath().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "path:'" + path + '\'' +
                ", name:'" + name + '\'' +
                ", bz:'" + bz + '\'' +
                ", id:" + id +
                ", isUpload:" + isUpload +
                '}';
    }
}
