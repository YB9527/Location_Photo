package com.xp.common.po;

import android.util.Log;

public class Redis {
    private Long id;
    private String mark;
    private String json;
    public Redis(){

    }
    public Redis(String mark, String json) {
        this.mark = mark;
        this.json = json;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "Redis{" +
                "id=" + id +
                ", mark='" + mark + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
