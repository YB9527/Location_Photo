package com.xp.common.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.xp.R;

public class TDTFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tdt, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init() {
        MapView mMapView =  view.findViewById(R.id.main_mapview);
//设置启用内置的缩放控件
        mMapView.setBuiltInZoomControls(true);
//得到mMapView的控制权,可以用它控制和驱动平移和缩放
        MapController mMapController = mMapView.getController();
//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
//设置地图中心点
        mMapController.setCenter(point);
//设置地图zoom级别
        mMapController.setZoom(12);
    }
}
