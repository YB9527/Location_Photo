package com.xp.xzqy.po;

/**
 * 行政代码， 选择性 封装
 */
public class XZDMVo {
    private XZDM xzdm;
    private Boolean isSelect;

    public XZDMVo() {

    }

    public XZDMVo(XZDM xzdm) {
        this.xzdm = xzdm;
    }


    public XZDM getXzdm() {
        return xzdm;
    }

    public void setXzdm(XZDM xzdm) {
        this.xzdm = xzdm;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
