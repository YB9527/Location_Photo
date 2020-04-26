package com.xp.zjd.po;

import android.graphics.Color;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.google.gson.annotations.Expose;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.ArcgisTool;
import com.xp.zjd.service.ZJDService;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.util.List;


public class ZJDGeometry {
    @Expose
    private Long id;
    @Expose
    private String ZDNUM;
    @Expose
    private String QUANLI;

    /**
     * arcgis 中长度，只有 线、面 图形有
     */
    @Expose
    private String SHAPE_Length;
    /**
     * arcgis 中面积，只有 面 图形有
     */
    @Expose
    private String SHAPE_Area;
    @Expose
    private String geometry;
    @Expose
    private String BZ;
    @Expose
    private String geometryStyle;
    @Expose
    private String geometryType;


    public ZJDGeometry() {
    }

    public ZJDGeometry(SpatialReference sp, Geometry geometry) {

        String json = GeometryEngine.geometryToJson(sp, geometry);
        //Point point = ArcgisTool.jsonToGeometry(json);

        this.geometry = json;
        switch (geometry.getType()) {
            case POINT:
                this.geometryType = "POINT";
                break;
            case LINE:
                this.geometryType = "LINE";
                break;
            case POLYGON:
                this.geometryType = "POLYGON";
                break;
            default:
                break;
        }
    }
    public String getSHAPE_Length() {
        return SHAPE_Length;
    }

    public void setSHAPE_Length(String SHAPE_Length) {
        this.SHAPE_Length = SHAPE_Length;
    }

    public String getSHAPE_Area() {
        return SHAPE_Area;
    }

    public void setSHAPE_Area(String SHAPE_Area) {
        this.SHAPE_Area = SHAPE_Area;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    private List<ZJDGeometry> zjdGeometry;//考虑多部件情况


    public List<ZJDGeometry> getZjdGeometry() {
        return zjdGeometry;
    }

    public void setZjdGeometry(List<ZJDGeometry> zjdGeometry) {
        this.zjdGeometry = zjdGeometry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZDNUM() {
        return ZDNUM;
    }

    public void setZDNUM(String ZDNUM) {
        this.ZDNUM = ZDNUM;
    }

    public String getQUANLI() {
        return QUANLI;
    }

    public void setQUANLI(String QUANLI) {
        this.QUANLI = QUANLI;
    }

    public String getGeometryStyle() {
        return geometryStyle;
    }

    public void setGeometryStyle(String geometryStyle) {
        this.geometryStyle = geometryStyle;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }


    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }


}
