package com.xp.xzqy.po;

import com.google.gson.annotations.Expose;
import com.xp.zjd.po.ZJD;

import java.util.List;

/**
 * 行政区域
 */
public class XZDM {
    @Expose
    private Long id;
    @Expose
    private String DJZQDM;
    @Expose
    private String DJZQMC;

    private List<ZJD> zjds;

    public XZDM(){

    }

    public XZDM(String DJZQDM, String DJZQMC) {
        this.DJZQDM = DJZQDM;
        this.DJZQMC = DJZQMC;
    }

    public List<ZJD> getZjds() {
        return zjds;
    }

    public void setZjds(List<ZJD> zjds) {
        this.zjds = zjds;
    }

    @Override
    public String toString() {
        return "XZDM{" +
                "id=" + id +
                ", DJZQDM='" + DJZQDM + '\'' +
                ", DJZQMC='" + DJZQMC + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDJZQDM() {
        return DJZQDM;
    }

    public void setDJZQDM(String DJZQDM) {
        this.DJZQDM = DJZQDM;
    }

    public String getDJZQMC() {
        return DJZQMC;
    }

    public void setDJZQMC(String DJZQMC) {
        this.DJZQMC = DJZQMC;
    }
}
