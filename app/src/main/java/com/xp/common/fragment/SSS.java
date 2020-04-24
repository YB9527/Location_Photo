package com.xp.common.fragment;

import android.content.Context;
import android.location.Location;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import javax.microedition.khronos.opengles.GL10;

/**
 * 天地图定位
 */
public class SSS {

    /**
     * 上下文对象
     */
    private Context context;

    /**
     * 地图对象
     */
    private MapView mapView;

    /**
     * 定位辅助对象
     */
    private LocationOverlay overlay;

    /**
     * 是否开启一次性定位
     */
    private boolean onceLocation = true;


    /**
     * 构造函数
     * @param context
     * @param mapView
     */
    public SSS(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
    }


    /**
     * 获取定位覆盖物
     * @return
     */
    public LocationOverlay getOverlay() {
        return overlay;
    }

    /**
     * 设置定位覆盖物
     * @param overlay
     */
    public void setOverlay(LocationOverlay overlay) {
        this.overlay = overlay;
    }

    /**
     * 是否一次性定位
     * @return
     */
    public boolean isOnceLocation() {
        return onceLocation;
    }

    /**
     * 是否一次性定位
     * @param onceLocation
     */
    public void setOnceLocation(boolean onceLocation) {
        this.onceLocation = onceLocation;
    }

    /**
     * 获取定位监听
     * @return
     */
    public OnSkyLangLocationChangedListener getListener() {
        return listener;
    }

    /**
     * 设置定位监听
     * @param listener
     */
    public void setListener(OnSkyLangLocationChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 开始定位
     */
    public void start() {
        overlay = new LocationOverlay(context, mapView);
        mapView.addOverlay(overlay);
        overlay.enableCompass();
        overlay.enableMyLocation();
        overlay.setGpsFollow(true);
        overlay.setVisible(true);
    }

    /**
     * 停止定位
     */
    public void stop() {
        if (overlay != null) {
            if (overlay.isCompassEnabled()) {
                overlay.disableCompass();
            }
            if (overlay.isMyLocationEnabled()) {
                overlay.disableMyLocation();
            }
        }
    }

    private class LocationOverlay extends MyLocationOverlay {

        public LocationOverlay(Context context, MapView mapView) {
            super(context, mapView);
        }

        @Override
        public void onLocationChanged(Location location) {
            super.onLocationChanged(location);
            if (listener != null) {
                listener.onSkyLangLocationChanged(location);
            }
            if (onceLocation) {
                stop();
            }
        }
    }

    /**
     * 定位监听接口
     */
    public OnSkyLangLocationChangedListener listener;

    public interface OnSkyLangLocationChangedListener {
        void onSkyLangLocationChanged(Location location);
    }

}

