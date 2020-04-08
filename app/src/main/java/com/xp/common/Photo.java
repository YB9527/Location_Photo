package com.xp.common;

import java.io.File;

/**
 * Created by Administrator on 2020/3/31.
 */
public class Photo {
    private String path;
    private String name;
    private String describe;
    private Integer id;
    /**
     * 是否已经上传文件
     */
    private Boolean isUpload;

    public Photo(String path,boolean isUpload) {
        setPath(path);
        this.isUpload =isUpload;
    }
    public Photo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getUpload() {
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

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPath() {
        if(path == null){
            return  "";
        }
        return path;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
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
}
