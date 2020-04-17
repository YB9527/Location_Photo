package com.xp.usermanager.po;



public enum Level {
    zero(0,"最高"),
    one(1,"一般"),
    two(2,"查看"),
    three(3,"只能查看自己");

    private String value;
    private int levelNum;
    private Level(int levelNum,String value) {
        this.value = value;
        this.levelNum =levelNum;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
