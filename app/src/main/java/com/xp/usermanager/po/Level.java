package com.xp.usermanager.po;



public enum Level {
    manager(0, "管理员","manager"),
    employee(1, "工作人员","employee"),
    government(2, "政府人员","government"),
    peasant(3, "农户","peasant");

    private String describe;
    private int levelNum;
    private String name;
    private Level(int levelNum, String describe,String name) {
        this.describe = describe;
        this.levelNum = levelNum;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 转 json 时 使用
     *
     * @return
     */
    @Override
    public String toString() {
        return "{" +
                "describe:'" + describe + '\'' +
                ", levelNum:" + levelNum +
                ", name:'" + name + '\'' +
                '}';
    }
}
