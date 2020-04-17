package com.xp.zjd.fragments;

import android.graphics.Color;
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
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.xp.MainActivity;
import com.xp.R;
import com.xp.common.AndroidTool;
import com.xp.common.ArcgisTool;
import com.xp.common.Tool;
import com.xp.zjd.photo.PhotosFragment;
import com.xp.zjd.po.MapListenerEnum;
import com.xp.zjd.po.ZJD;
import com.xp.zjd.service.ZJDService;

import org.codehaus.jackson.JsonFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private List<Point> pointList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.fragment_zjd_arcgismap, container, false);
        this.view = view;

        init();
        buttonAddLister();
        return view;
    }

    /**
     * map 的初始化
     */
    private void init() {
        Button btu = view.findViewById(R.id.test);
        btu.setOnClickListener(new TestLister());
        mMapView = view.findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.WHITE, 0, 0);//设置背景
        this.layers = mMapView.getLayers();

        //String mapServerUrl = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";
        //ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(mapServerUrl);
        //mMapView.addLayer(arcGISTiledMapServiceLayer);


        String path = ZJDService.getGeodatabasePath();
        try {
            final Geodatabase localGdb = new Geodatabase(path);
            if (localGdb != null) {
                for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                    FeatureLayer featureLayer = new FeatureLayer(gdbFeatureTable);
                    mMapView.addLayer(featureLayer);
                    featureLayer.selectFeatures(new QueryParameters(), FeatureLayer.SelectionMode.ADD, new CallbackListener<FeatureResult>() {

                        @Override
                        public void onCallback(FeatureResult objects) {
                            long count = objects.featureCount();
                            /*while (objects.iterator().hasNext()) {
                                // 获取查询结果的第一个要素的地图范围，并设置地图缩放至该范围
                                Feature feature = (Feature) objects.iterator().next();
                                String Polygonjson =  GeometryEngine.geometryToJson(feature.getSpatialReference() ,feature.getGeometry());
                                SpatialReference sp =  localtilayer1.getSpatialReference();
                                String s = Polygonjson+"555";
                                JsonFactory jsonFactory = new JsonFactory();
                                try {
                                    MapGeometry mapGeometry2 = GeometryEngine.jsonToGeometry(jsonFactory.createJsonParser(Polygonjson));
                                   // GeodatabaseFeature f = new GeodatabaseFeature();
                                    s = Polygonjson+"555";
                                     s = Polygonjson+"555";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }*/
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
                    //String name =  featureLayer.getFeatureTable().getTableName();
                    //String name2 = featureLayer.getName();
                    if (featureLayer.getName().equals("宅基地")) {
                        mMapView.setExtent(featureLayer.getExtent());
                        //mMapView.setMaxScale(15000);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File("/data/data/com.xp/files/zjd/tpks/无标题.tpk");
        if (file.exists()) {
            localtilayer1 = new ArcGISLocalTiledLayer(file.getAbsolutePath());

            SpatialReference sp =  localtilayer1.getSpatialReference();
            SpatialReference sh = localtilayer1.getSpatialReference();
            mMapView.addLayer(localtilayer1);
            mMapView.setExtent(localtilayer1.getExtent());
        }

        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);

        this.layers = mMapView.getLayers();
        mMapView.setOnSingleTapListener(new TapListener());
    }

    private GraphicsLayer mGraphicsLayer;


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

        Button btuFinish = view.findViewById(R.id.btuFinish);
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
                this.pointList = new ArrayList<>();
                mGraphicsLayer.removeAll();
                break;
            case R.id.btuFinish:

                break;

        }
    }

    /**
     * 更改地图
     */
    private void changeBottomDrawing() {
        Toast.makeText(AndroidTool.getMainActivity(), "正在研发中", Toast.LENGTH_SHORT).show();
    }

    ArcGISLocalTiledLayer localtilayer1;


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
                    drawPoint(mapPoint);
                    break;
                case DrawLine:
                    drawLine(mapPoint);
                    break;
                case DrawPolygon:
                    drawPolygon(mapPoint);
                    break;
                case LookMessage:
                    lookMessage(mapPoint);
                    break;
                case None:

                    break;
            }
        }

        /**
         * 绘制面
         *
         * @param mapPoint
         */
        private void drawPolygon(Point mapPoint) {
            mGraphicsLayer.removeAll();
            pointList.add(mapPoint);

            Polygon polygon = new Polygon();
            for (int i = 0; i < pointList.size(); i++) {
                if (i == 0) {
                    polygon.startPath(pointList.get(i)); //起点
                } else {
                    polygon.lineTo(pointList.get(i));
                }
            }

            //点
            //SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(Color.GREEN, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
            // Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);
            //mGraphicsLayer.addGraphic(pointGraphic);

            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 300, SimpleLineSymbol.STYLE.SOLID);
            SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.YELLOW);
            fillSymbol.setOutline(lineSymbol);

            Graphic graphic = new Graphic(polygon, fillSymbol);
            mGraphicsLayer.addGraphic(graphic);

        }

        /**
         * 绘制线
         *
         * @param mapPoint
         */
        private void drawLine(Point mapPoint) {
            mGraphicsLayer.removeAll();
            pointList.add(mapPoint);
            Polyline polyline = new Polyline();
            if (pointList.size() > 1) {
                for (int i = 0; i < pointList.size(); i++) {
                    if (i == 0) {
                        polyline.startPath(pointList.get(i));
                    } else {
                        polyline.lineTo(pointList.get(i));
                    }
                }
            }
            //点
            SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
            Graphic pointGraphic = new Graphic(mapPoint, simpleMarkerSymbol);
            mGraphicsLayer.addGraphic(pointGraphic);

            Graphic graphic = new Graphic(polyline, new SimpleLineSymbol(Color.RED, 300, SimpleLineSymbol.STYLE.SOLID));
            mGraphicsLayer.addGraphic(graphic);
        }

        /**
         * 绘制点
         *
         * @param mapPoint
         */
        private void drawPoint(Point mapPoint) {


            Graphic graphic = new Graphic(mapPoint, new SimpleMarkerSymbol
                    (Color.RED, 30, SimpleMarkerSymbol.STYLE.CIRCLE));
            mGraphicsLayer.addGraphic(graphic);

            SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 7, SimpleMarkerSymbol.STYLE.CIRCLE);
            mGraphicsLayer.addGraphic(new Graphic(mapPoint, simpleMarkerSymbol));


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
                        FeatureTable table = featureLayer.getFeatureTable();
                        boolean flag = table.hasGeometry();
                        List<Map<String, Object>> dksMap = lookMessage(mapPoint, featureLayer);
                        List<ZJD> zjds = ArcgisTool.FeatureResultToObj(dksMap, ZJD.class);
                        if (!Tool.isEmpty(zjds)) {
                            ZJDDialog zjdDialog = ZJDDialog.newInstance(zjds);
                            zjdDialog.show(getFragmentManager(), "zjddialog");

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
         * @param mapPoint     被点击的map 点
         * @param featureLayer 查看的图层
         * @return
         * @throws ExecutionException
         * @throws InterruptedException
         */
        private List<Map<String, Object>> lookMessage(Point mapPoint, FeatureLayer featureLayer) throws ExecutionException, InterruptedException {
            QueryParameters q = new QueryParameters();
            q.setSpatialRelationship(SpatialRelationship.INTERSECTS);
            q.setGeometry(mapPoint);
            //q.setGeometry(new Point(493658.506, 3463379.233));
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

    private class TestLister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Layer zjdLayer = null;
            for (Layer layer : layers
            ) {
                if (layer.getName() != null && layer.getName().equals("宅基地")) {
                    Polygon polygon = layer.getExtent();

                    Envelope ev = new Envelope();
                    polygon.queryEnvelope(ev);
                    mMapView.setExtent(ev);
                }
            }
        }
    }
}
