package com.xp.zjd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.esri.android.map.FeatureLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.xp.R;
import com.xp.common.AndroidTool;
import com.xp.common.ArcgisTool;
import com.xp.zjd.photo.PhotosFragment;
import com.xp.zjd.po.MapListenerEnum;
import com.xp.zjd.po.ZJD;
import com.xp.zjd.service.ZJDService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ZJDArcgisMap extends Fragment implements View.OnClickListener {
    private View view;
    private MapView mMapView;
    /**
     * 初始化是什么都没有点
     */
    private static MapListenerEnum mapListenerEnum = MapListenerEnum.None;
    private Layer[] layers;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.fragment_zjd_arcgismap, container, false);
        this.view = view;
        init();
        buttonAddLister();
        mapInit();
        return view;
    }

    /**
     * map 的初始化
     */
    private void mapInit() {
        this.layers = mMapView.getLayers();
    }

    /**
     * 地图 button添加监听事件
     */
    private void buttonAddLister() {
        Button btuAddNotesPoint = view.findViewById(R.id.addNotesPoint);
        btuAddNotesPoint.setOnClickListener(this);
        Button btuAddNotesLine = view.findViewById(R.id.addNotesLine);
        btuAddNotesLine.setOnClickListener(this);
        Button btuAddNoteslPolygo = view.findViewById(R.id.addNoteslPolygon);
        btuAddNoteslPolygo.setOnClickListener(this);
        Button btuLookMessage = view.findViewById(R.id.lookMessage);
        btuLookMessage.setOnClickListener(this);
        Button btuChangeBottomDrawing = view.findViewById(R.id.changeBottomDrawing);
        btuChangeBottomDrawing.setOnClickListener(this);
        Button btuClearMapState = view.findViewById(R.id.clearMapState);
        btuClearMapState.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNotesPoint:
                mapListenerEnum = MapListenerEnum.DrawPoint;
                break;
            case R.id.addNotesLine:
                mapListenerEnum = MapListenerEnum.DrawLine;
                break;
            case R.id.addNoteslPolygon:
                mapListenerEnum = MapListenerEnum.DrawPolygon;
                break;
            case R.id.lookMessage:
                mapListenerEnum = MapListenerEnum.LookMessage;
                break;
            case R.id.changeBottomDrawing:
                changeBottomDrawing();
                break;
            case R.id.clearMapState:
                mapListenerEnum = MapListenerEnum.None;
                break;

        }
    }

    /**
     * 更改地图
     */
    private void changeBottomDrawing() {
        Toast.makeText(AndroidTool.getMainActivity(), "正在研发中", Toast.LENGTH_SHORT);
    }

    ArcGISLocalTiledLayer localtilayer1;

    private void init() {
        mMapView = view.findViewById(R.id.map);
        String path = ZJDService.getGeodatabasePath();
        try {
            final Geodatabase localGdb = new Geodatabase(path);
            if (localGdb != null) {
                for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                    if (gdbFeatureTable.hasGeometry()) {
                        FeatureLayer featureLayer = new FeatureLayer(gdbFeatureTable);
                        mMapView.addLayer(featureLayer);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //localtilayer1 = new ArcGISLocalTiledLayer(getFilesDir()+"/tpks/无标题.tpk");
        //mMapView.addLayer(localtilayer1);
        //mMapView.setExtent(localtilayer1.getExtent());
        mMapView.setOnSingleTapListener(new TapListener());
    }

    /**
     * Map 点击事件处理
     */
    private class TapListener implements OnSingleTapListener {
        @Override
        public void onSingleTap(float x, float y) {
            Point mapPoint = mMapView.toMapPoint(x, y);
            mapClick(mapPoint, mapListenerEnum);
        }

        /**
         * 地图点击事件
         *
         * @param mapPoint
         * @param mapListenerEnum
         */
        private void mapClick(Point mapPoint, MapListenerEnum mapListenerEnum) {
            //Toast.makeText(view.getContext(), "当前状态：" + mapListenerEnum, Toast.LENGTH_SHORT).show();
            switch (mapListenerEnum) {
                case DrawPoint:
                    break;
                case DrawLine:
                    break;
                case DrawPolygon:
                    break;
                case LookMessage:
                    lookMessage(mapPoint);
                    break;
                case None:

                    break;
            }
        }



        /**
         * 查看被选择的面
         *
         * @param mapPoint
         */
        private void lookMessage(Point mapPoint) {
            for (Layer layer : layers
            ) {
                if (layer instanceof FeatureLayer) {
                    FeatureLayer featureLayer = (FeatureLayer) layer;
                    try {
                        List<Map<String, Object>> dksMap = lookMessage(mapPoint, featureLayer);
                        List<ZJD> zjds = ArcgisTool.FeatureResultToObj(dksMap,ZJD.class);
                        if(dksMap.size() > 0){
                            ZJDDialog zjdDialog =  ZJDDialog.newInstance(dksMap);
                            zjdDialog.show(getFragmentManager(),"zjddialog");
                            //ZJDDialog zjdDialog = new ZJDDialog(AndroidTool.getMainActivity(),dksMap);
                            //zjdDialog.show();
                            //TestFragment dialog=new TestFragment();
                            //dialog.show(getFragmentManager(),"dialog");



                              /*  FragmentTransaction ft = getFragmentManager().beginTransaction();
                                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                                if (prev != null) {
                                    ft.remove(prev);
                                }
                                ft.addToBackStack(null);

                                // Create and show the dialog.
                                DialogFragment newFragment = TextDialogFramnet.newInstance(0);
                                newFragment.show(ft, "dialog");*/

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        /**
         * 得到第一个 feature 图形数据
         *
         * @param mapPoint 被点击的map 点
         * @param featureLayer  查看的图层
         * @return
         * @throws ExecutionException
         * @throws InterruptedException
         */
        private List<Map<String, Object>> lookMessage(Point mapPoint, FeatureLayer featureLayer) throws ExecutionException, InterruptedException {
            QueryParameters q = new QueryParameters();
            q.setSpatialRelationship(SpatialRelationship.INTERSECTS);
            q.setGeometry(new Point(mapPoint.getX(), mapPoint.getY()));
            Future<FeatureResult> rs = featureLayer.selectFeatures(q, FeatureLayer.SelectionMode.NEW, new CallbackListener<FeatureResult>() {
                @Override
                public void onCallback(FeatureResult objects) {

                }
                @Override
                public void onError(Throwable throwable) {

                }
            });
            FeatureResult fr = rs.get();
            List<Map<String, Object>> dksMap = new ArrayList<>();
            if (fr.iterator().hasNext()) {
                // 获取查询结果的第一个要素的地图范围，并设置地图缩放至该范围
                Feature feature = (Feature) fr.iterator().next();
                Map<String, Object> map = feature.getAttributes();
                dksMap.add(map);
            }
            return dksMap;

        }
    }

}
