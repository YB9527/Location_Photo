package com.xp.zjd.po;

/**
 * 地图发生的点击事件
 */
public enum  MapListenerEnum {
    /**
     * 清除地图状态
     */
    None,
    /**
     * 画点
     */
    DrawPoint,
    /**
     * 画线
     */
    DrawLine,
    /**
     * 画面
     */
    DrawPolygon,
    /**
     * 查看地块信息
     */
    LookMessage
}
