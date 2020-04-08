 package com.xp.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.MapView;
import com.xp.R;

 public class MapFragment extends Fragment {

     private MapView mMapView = null;

     @Nullable
     @Override
     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_map, container, false);
         mMapView = (MapView) view.findViewById(R.id.bmapView);
         return view;
     }
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);


         //获取地图控件引用

     }
     @Override
     public void onResume() {
         super.onResume();
         //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
         mMapView.onResume();
     }
     @Override
     public void onPause() {
         super.onPause();
         //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
         mMapView.onPause();
     }
     @Override
     public void onDestroy() {
         super.onDestroy();
         //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
         mMapView.onDestroy();
     }
 }
