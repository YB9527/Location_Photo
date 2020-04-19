package com.xp.zjd.po;

import com.google.gson.annotations.Expose;
import com.xp.common.tools.FileTool;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSelect {
    @Expose
    private String name;
    @Expose
    private Double fileSize;
    @Expose
    private Date fileCreatedate;
    @Expose
    private Boolean isselect;
    @Expose
    private String path;
    private File file;

    public FileSelect() {

        isselect = false;
    }

    public FileSelect(File file) {

        this.isselect = false;
        this.name = FileTool.getFileName(file.getAbsolutePath());
        this.path = file.getAbsolutePath();
        this.fileSize = file.length() / 1024d / 1024d;
        this.fileCreatedate = FileTool.getCreateDate(file);
    }

    /**
     * 得到 选中 的文件
     *
     * @param fileSelects
     * @return
     */
    public static List<FileSelect> getSelectedFile(List<FileSelect> fileSelects) {
        List<FileSelect> results = new ArrayList<>();
        for (FileSelect fileSelect : fileSelects
        ) {
            if (fileSelect.isselect) {
                results.add(fileSelect);
            }
        }
        return results;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public Date getFileCreatedate() {
        return fileCreatedate;
    }

    public void setFileCreatedate(Date fileCreatedate) {
        this.fileCreatedate = fileCreatedate;
    }

    public Boolean getIsselect() {
        return isselect;
    }

    public void setIsselect(Boolean isselect) {
        this.isselect = isselect;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
