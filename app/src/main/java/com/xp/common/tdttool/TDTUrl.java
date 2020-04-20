package com.xp.common.tdttool;

import java.util.Random;

public class TDTUrl {
    private TianDiTuTiledMapServiceType _tiandituMapServiceType;
    private int _level;
    private int _col;
    private int _row;

    public TDTUrl(int level, int col, int row, TianDiTuTiledMapServiceType tiandituMapServiceType) {
        this._level = level;
        this._col = col;
        this._row = row;
        this._tiandituMapServiceType = tiandituMapServiceType;
    }

    public String getUrl() {
        StringBuilder url1 = new StringBuilder("http://t");
        Random random = new Random();
        int subdomain = (random.nextInt(6) + 1);

        url1.append(subdomain);
        switch (this._tiandituMapServiceType) {
            case VEC_C://矢量
                url1.append(".tianditu.com/DataServer?T=").append("vec_w").append("&X=").append(this._col).append("&Y=")
                        .append(this._row).append("&L=").append(this._level)
                        .append("&tk=b23fbe0ee1e8e76533866b75f9d5ed05");
                break;
            case CVA_C://矢量注记
                url1.append(".tianditu.com/DataServer?T=").append("cva_w").append("&X=").append(this._col).append("&Y=")
                        .append(this._row).append("&L=").append(this._level)
                        .append("&tk=b23fbe0ee1e8e76533866b75f9d5ed05");
                break;
            case CIA_C://影像注记
                url1.append(".tianditu.com/DataServer?T=").append("cia_w").append("&X=").append(this._col).append("&Y=")
                        .append(this._row).append("&L=").append(this._level)
                        .append("&tk=b23fbe0ee1e8e76533866b75f9d5ed05");
                break;
            case IMG_C://影像
                url1.append(".tianditu.com/DataServer?T=").append("img_w").append("&X=").append(this._col).append("&Y=")
                        .append(this._row).append("&L=").append(this._level)
                        .append("&tk=b23fbe0ee1e8e76533866b75f9d5ed05");

                break;
            default:
                break;
        }
        //System.out.println(url1.toString());
        return url1.toString();

    }
}
