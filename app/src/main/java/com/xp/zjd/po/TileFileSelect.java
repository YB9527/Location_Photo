package com.xp.zjd.po;

import java.io.File;

public class TileFileSelect extends FileSelect {
    public TileFileSelect() {
        super();
    }
    public TileFileSelect(File file) {
        super(file);
    }


    public static TileFileSelect getInstance(File file) {
        if (file.exists()) {
            return new TileFileSelect(file);
        }
        return null;
    }
}
