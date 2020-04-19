package com.xp.zjd.po;

import java.io.File;

public class GeodatabaseFileSelect extends FileSelect {
    public GeodatabaseFileSelect() {
        super();
    }

    /**
     * 不检查文件 是否存在
     * @param file
     */
    private GeodatabaseFileSelect(File file) {
        super(file);
    }

    /**
     * 检查 文件是否存在  ，如果不存在 就返回 null
     * @param file
     * @return
     */
    public static GeodatabaseFileSelect getInstance(File file) {
        if (file.exists()) {
            return new GeodatabaseFileSelect(file);
        }
        return null;
    }
}
