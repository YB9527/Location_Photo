package com.xp.zjd.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.google.gson.reflect.TypeToken;
import com.xp.R;
import com.xp.common.po.MyCallback;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;
import com.xp.common.tdttool.TianDiTuTiledMapServiceLayer;
import com.xp.common.tdttool.TianDiTuTiledMapServiceType;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.ArcgisTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.Tool;
import com.xp.zjd.photo.PhotoService;
import com.xp.zjd.po.MapListenerEnum;
import com.xp.zjd.po.ZJD;
import com.xp.zjd.po.ZJDGeometry;
import com.xp.zjd.service.ZJDService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ZJDArcgisMap extends Fragment implements View.OnClickListener {

    private MapView mMapView;

    //本地图形容器  //服务器中的图形容器
    private GraphicsLayer graphicsLayer;
    //临时图形 容器
    private GraphicsLayer drawGraphicsLayer;
    //点集合
    private List<Point> pointList = new ArrayList<>();
    ArcGISLocalTiledLayer localTiledLayer;
    private View view;
    private SpatialReference sp;
    private List<ZJD> zjds;
    /*
     localtilayer1 = new ArcGISLocalTiledLayer(file.getAbsolutePath());
            mMapView.addLayer(localtilayer1);
     */
    /**
     * 初始化是屏幕什么都没有点
     */
    private static MapListenerEnum mapListenerEnum = MapListenerEnum.None;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.fragment_zjd_arcgismap, container, false);
        this.view = view;
        zjds = new ArrayList<>();
        init();
        //按钮初始化
        buttonAddLister();
        //定位到上次点
        locationLastPoint();

        return view;
    }

    /**
     * 定位到上次点
     */
    private void locationLastPoint() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AndroidTool.showProgressBar();
                    Thread.sleep(3000); //休眠一秒
                    mMapView.setScale(1000);
                    mMapView.setExtent(new Point(1.1748699318944164E7, 3350506.3943553544));
                    sp = mMapView.getSpatialReference();

                    //添加服务器中地块
                    //添加本地地块
                    addZJDToGraphic();

                    AndroidTool.closeProgressBar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private TapListener tapListener;

    /**
     * map 的初始化
     */
    private void init() {

        mMapView = view.findViewById(R.id.map);
        //地图事件监听
        tapListener = new TapListener();
        mMapView.setOnSingleTapListener(tapListener);
        //去除背后的网格
        mMapView.setMapBackground(0xffffff, 0xffffff, 1.0f, 100.0f);
        //去除水印(Licensed for Developer User Only)
        String clientID = "273DobVpQjOHcrZe";
        ArcGISRuntime.setClientId(clientID);

        graphicsLayer = new GraphicsLayer();
        mMapView.addLayer(graphicsLayer);
        drawGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(drawGraphicsLayer);

        //arcgis 自带底图
        //String mapServerUrl = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";
        //ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(mapServerUrl);
        //mMapView.addLayer(arcGISTiledMapServiceLayer);

        /**
         * 天地图矢量
         * */
        TianDiTuTiledMapServiceLayer tianDiTuTiledMapServiceLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.VEC_C);
        mMapView.addLayer(tianDiTuTiledMapServiceLayer, 0);
        /**
         * 天地图矢量标注
         * */
        TianDiTuTiledMapServiceLayer serviceLayer = new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CVA_C);
        mMapView.addLayer(serviceLayer, 1);




        /*
        try {
            localGdb = new Geodatabase(getFilesDir() + "/zjd.geodatabase");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (localGdb != null) {
            for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                featureLayer = new FeatureLayer(gdbFeatureTable);
                mMapView.addLayer(featureLayer);
            }
        }*/

    }

    private void addZJDToGraphic(List<ZJD> zjdsTem) {

        zjds.addAll(zjdsTem);
        for (ZJD zjd : zjdsTem
        ) {
            for (ZJDGeometry zjdGeometry : zjd.getZjdGeometry()
            ) {
                Geometry geometry = ArcgisTool.jsonToGeometry(zjdGeometry.getGeometry());
                addZJDGraphic(geometry, zjd);
            }
        }
    }
    /**
     * 从本地 和web 加载地块
     */
    private void addZJDToGraphic() {

        List<ZJD> localZJDs = ZJDService.findLoaclZJDs();
        addZJDToGraphic(localZJDs);
        //添加web 宅基地
        ZJDService.findWebZJDs(new MyCallback() {
            @Override
            public void call(ResultData result) {
                if(result.getStatus() == Status.Success){
                    ResultData resultData = OkHttpClientUtils.resposeToResultData(result.getResponse(),new TypeToken<List<ZJD>>(){}.getType());
                    if(resultData.getObject() != null){
                        List<ZJD> webZJDs =(List<ZJD>)resultData.getObject();
                        addZJDToGraphic(webZJDs);
                    }
                }else{
                    AndroidTool.showAnsyTost(result.getMessage(),Status.Error);
                }
            }
        });
    }
    /**
     * 添加到 mapview
     *
     * @param geometry
     * @param zjd
     */
    private void addZJDGraphic(Geometry geometry, ZJD zjd) {
        int hasePhoto = Color.GREEN;
        int noPhoto = Color.RED;

        Map<String, Object> map = new HashMap<>();
        map.put("zjd", Tool.objectToJson(zjd));
        Graphic graphic = null;
        switch (geometry.getType()) {
            case POINT:
                if (zjd.getPhotos().size() > 0) {
                    graphic = new Graphic(geometry, new SimpleMarkerSymbol
                            (hasePhoto, 8, SimpleMarkerSymbol.STYLE.CIRCLE), map);
                } else {
                    graphic = new Graphic(geometry, new SimpleMarkerSymbol
                            (noPhoto, 8, SimpleMarkerSymbol.STYLE.CIRCLE), map);
                }

                break;
            case POLYLINE:
                if (zjd.getPhotos().size() > 0) {
                    graphic = new Graphic(geometry, new SimpleLineSymbol(hasePhoto, 3, SimpleLineSymbol.STYLE.SOLID), map);
                } else {
                    graphic = new Graphic(geometry, new SimpleLineSymbol(noPhoto, 3, SimpleLineSymbol.STYLE.SOLID), map);
                }
                break;
            case POLYGON:
                SimpleLineSymbol lineSymbol;
                SimpleFillSymbol fillSymbol;
                if (zjd.getPhotos().size() > 0) {
                    lineSymbol = new SimpleLineSymbol(Color.RED, 1, SimpleLineSymbol.STYLE.SOLID);
                    fillSymbol = new SimpleFillSymbol(hasePhoto);
                } else {
                    lineSymbol = new SimpleLineSymbol(Color.RED, 1, SimpleLineSymbol.STYLE.SOLID);
                    fillSymbol = new SimpleFillSymbol(noPhoto);
                }
                fillSymbol.setOutline(lineSymbol);
                graphic = new Graphic(geometry, fillSymbol, map);
                break;
            default:
                break;
        }
        drawGraphicsLayer.removeAll();
        if (graphic != null) {
            graphicsLayer.addGraphic(graphic);
        }

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
        Button btuClearMapState = view.findViewById(R.id.clearMapState);
        btuClearMapState.setOnClickListener(this);

        Button btuFinish = view.findViewById(R.id.btuFinish);
        btuFinish.setOnClickListener(this);
        Button btuClearDraw = view.findViewById(R.id.btuClearDraw);
        btuClearDraw.setOnClickListener(this);

    }

    /**
     * 按钮点击处理
     *
     * @param v
     */
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
            case R.id.clearMapState:
                mapListenerEnum = MapListenerEnum.None;
                break;
            case R.id.btuFinish:
                drawFinish();
            case R.id.btuClearDraw:
                clearDraw();
                break;
            default:
                break;

        }
    }

    /**
     * 清空正在画的图形
     */
    private void clearDraw() {
        this.pointList.clear();
        this.drawGraphicsLayer.removeAll();
    }

    /**
     * 线和面完成事件
     */
    private void drawFinish() {

        switch (mapListenerEnum) {
            case DrawLine:
                tapListener.drawLine(null, true);
                break;
            case DrawPolygon:
                tapListener.drawPolygon(null, true);
                break;
            default:
                break;
        }
    }

    /**
     * Map 点击事件处理
     */
    private class TapListener implements OnSingleTapListener {
        private List<ZJD> zjds;

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
                    drawLine(mapPoint, false);
                    break;
                case DrawPolygon:
                    drawPolygon(mapPoint, false);
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
         * @param mapPoint 保存到数据库传入 null
         * @param isFinish
         */
        private void drawPolygon(final Point mapPoint, boolean isFinish) {
            if (!isFinish) {
                drawGraphicsLayer.removeAll();
            }
            if (mapPoint != null) {
                pointList.add(mapPoint);
            }
            if (pointList.size() <= 2) {
                //两个点不构成面
                return;
            }
            final Polygon polygon = new Polygon();
            for (int i = 0; i < pointList.size(); i++) {
                if (i == 0) {
                    polygon.startPath(pointList.get(i)); //起点
                } else {
                    polygon.lineTo(pointList.get(i));
                }
            }
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 1, SimpleLineSymbol.STYLE.SOLID);
            SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.YELLOW);
            fillSymbol.setOutline(lineSymbol);
            Graphic graphic;
            if (!isFinish) {
                graphic = new Graphic(polygon, fillSymbol);
                drawGraphicsLayer.addGraphic(graphic);
            } else {
                final List<ZJD> addZJDLists = new ArrayList<>();
                final ZJD zjd = new ZJD();
                ZJDGeometry geometry = new ZJDGeometry(sp, polygon);
                zjd.getZjdGeometry().add(geometry);
                addZJDLists.add(zjd);
                MapDKDialog zjdDialog = MapDKDialog.newInstance(zjds, addZJDLists, new MyCallback() {
                    @Override
                    public void call(ResultData result) {
                        if (result.getStatus() == Status.Error) {
                            AndroidTool.showAnsyTost(result.getMessage(), Status.Error);
                        } else {
                            //检查宗地编码有没有重复的，有的话，不能保存
                            addZJDGraphic(polygon, zjd);
                            AndroidTool.showAnsyTost("保存成功：" + zjd.getZDNUM(), Status.Success);
                        }
                    }
                });
                //弹出 地块窗口
                zjdDialog.show(getFragmentManager());
            }

        }

        /**
         * 绘制线
         *
         * @param mapPoint
         * @param isFinish
         */
        private void drawLine(Point mapPoint, boolean isFinish) {
            if (!isFinish) {
                drawGraphicsLayer.removeAll();
            }
            if (mapPoint != null) {
                pointList.add(mapPoint);
            }
            if (pointList.size() <= 1) {
                //还不构成线
                return;
            }
            final Polyline polyline = new Polyline();
            if (pointList.size() > 1) {
                for (int i = 0; i < pointList.size(); i++) {
                    if (i == 0) {
                        polyline.startPath(pointList.get(i));
                    } else {
                        polyline.lineTo(pointList.get(i));
                    }
                }
            }
            Graphic graphic;
            if (!isFinish) {
                graphic = new Graphic(polyline, new SimpleLineSymbol(Color.RED, 3, SimpleLineSymbol.STYLE.SOLID));
                drawGraphicsLayer.addGraphic(graphic);
            } else {
                final List<ZJD> addZJDLists = new ArrayList<>();
                final ZJD zjd = new ZJD();
                ZJDGeometry geometry = new ZJDGeometry(sp, polyline);
                zjd.getZjdGeometry().add(geometry);
                addZJDLists.add(zjd);
                MapDKDialog zjdDialog = MapDKDialog.newInstance(zjds, addZJDLists, new MyCallback() {
                    @Override
                    public void call(ResultData result) {
                        if (result.getStatus() == Status.Error) {
                            AndroidTool.showAnsyTost(result.getMessage(), Status.Error);
                        } else {
                            //检查宗地编码有没有重复的，有的话，不能保存
                            addZJDGraphic(polyline, zjd);
                            AndroidTool.showAnsyTost("保存成功：" + zjd.getZDNUM(), Status.Success);
                        }
                    }
                });
                //弹出 地块窗口
                zjdDialog.show(getFragmentManager());
            }
        }

        /**
         * 绘制点
         *
         * @param mapPoint
         */
        private void drawPoint(final Point mapPoint) {
            //弹出窗口
            //填写地块信息（地块名称，编码，备注信息）
            final List<ZJD> addZJDLists = new ArrayList<>();
            final ZJD zjd = new ZJD();
            ZJDGeometry geometry = new ZJDGeometry(sp, mapPoint);
            zjd.getZjdGeometry().add(geometry);
            addZJDLists.add(zjd);
            MapDKDialog zjdDialog = MapDKDialog.newInstance(zjds, addZJDLists, new MyCallback() {
                @Override
                public void call(ResultData result) {
                    if (result.getStatus() == Status.Error) {
                        AndroidTool.showAnsyTost(result.getMessage(), Status.Error);
                    } else {
                        //检查宗地编码有没有重复的，有的话，不能保存
                        addZJDGraphic(mapPoint, zjd);
                        AndroidTool.showAnsyTost("保存成功：" + zjd.getZDNUM(), Status.Success);
                    }
                }
            });
            //弹出 地块窗口
            zjdDialog.show(getFragmentManager());
        }

        /**
         * 查看被选择的面
         *
         * @param mapPoint
         */
        private void lookMessage(Point mapPoint) {

            List<ZJD> zjds = findGraphics(mapPoint, graphicsLayer);
            PhotoService.addNativePhoto(zjds);
            if (!Tool.isEmpty(zjds)) {
                MapDKDialog zjdDialog = MapDKDialog.newInstance(this.zjds, zjds, new MyCallback() {
                    @Override
                    public void call(ResultData result) {

                    }
                });
                zjdDialog.show(getFragmentManager(), "zjddialog");
            }
        }

        /**
         * 得到多个 feature 图形数据
         *
         * @param mapPoint      被点击的map 点
         * @param graphicsLayer 查看的图层
         * @return
         * @throws ExecutionException
         * @throws InterruptedException
         */
        private List<ZJD> findGraphics(Point mapPoint, GraphicsLayer graphicsLayer) {
            Point pt = mMapView.toScreenPoint(mapPoint);
            int[] ids = graphicsLayer.getGraphicIDs((float) pt.getX(), (float) pt.getY(), 20);
            List<ZJD> zjds = new ArrayList<>();
            for (int i = 0; i < ids.length; i++) {
                Object obj = graphicsLayer.getGraphic(ids[i]).getAttributeValue("zjd");
                if (obj != null) {
                    ZJD zjd = Tool.JsonToObject((String) obj, ZJD.class);
                    zjds.add(zjd);
                }
            }
            return zjds;
        }
    }


    /**
     * 查看被选择的面
     *
     * @param mapPoint
     */
    private void lookMessage(Point mapPoint) {
        Layer[] layers = mMapView.getLayers();
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
                        MapDKDialog zjdDialog = MapDKDialog.newInstance(this.zjds, zjds, new MyCallback() {
                            @Override
                            public void call(ResultData result) {

                            }
                        });
                        zjdDialog.show(getFragmentManager(), "zjddialog");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 得到多个 feature 图形数据
     *
     * @param mapPoint     被点击的map 点
     * @param featureLayer 查看的图层
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private List<Map<String, Object>> lookMessage(Point mapPoint, FeatureLayer featureLayer) {
        QueryParameters q = new QueryParameters();
        q.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        q.setGeometry(mapPoint);
        Future<FeatureResult> rs = featureLayer.selectFeatures(q, FeatureLayer.SelectionMode.NEW, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        FeatureResult fr = null;
        List<Map<String, Object>> dksMap = null;
        try {
            fr = rs.get();
            dksMap = new ArrayList<>();
            if (fr.iterator().hasNext()) {
                // 获取查询结果的第一个要素的地图范围，并设置地图缩放至该范围
                Feature feature = (Feature) fr.iterator().next();
                Map<String, Object> map = feature.getAttributes();
                dksMap.add(map);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dksMap;

    }

    /**
     * 添加文本
     */
    private void addText(Point mapPoint) {
        TextSymbol textSymbol = new TextSymbol(5, "I is text", Color.RED);
        textSymbol.setSize(5);
        Graphic textGraphic = new Graphic(mapPoint, textSymbol);
        graphicsLayer.addGraphic(textGraphic);
    }

    /**
     * 添加图片
     */
    private void addImage(Point mapPoint) {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);
        Graphic graphic = new Graphic(mapPoint, pictureMarkerSymbol);
        graphicsLayer.addGraphic(graphic);
        mMapView.setExtent(mapPoint);
    }

    /**
     * 绘制圆
     */
    private void drawCircle(Point mapPoint) {

        double radius = 1; //半径
        // mGraphicsLayer.removeAll();
        pointList.add(mapPoint);

        Polyline polyline = new Polyline();
        if (pointList.size() == 2) {
            for (int i = 0; i < pointList.size(); i++) {
                if (i == 0) {
                    polyline.startPath(pointList.get(i)); //开始点
                } else {
                    polyline.lineTo(pointList.get(i));
                }
            }
            radius = polyline.calculateLength2D(); //计算半径长度
        }
        Polygon polygon = new Polygon();
        getCircle(pointList.get(0), radius, polygon);
    }

    private void getCircle(Point point, double radius, Polygon polygon) {
        polygon.setEmpty();
        //圆形的边线点集合
        Point[] points = getPoints(point, radius);
        polygon.startPath(points[0]);
        for (int i = 1; i < points.length; i++) {
            polygon.lineTo(points[i]);
        }

        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 3, SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);
        graphicsLayer.addGraphic(pointGraphic);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.YELLOW, 3);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.GREEN);
        fillSymbol.setOutline(lineSymbol);
        Graphic graphic = new Graphic(polygon, fillSymbol);

        graphicsLayer.addGraphic(graphic);
    }

    /**
     * 通过中心点和半径计算得出圆形的边线点集合
     *
     * @param center
     * @param radius
     * @return
     */
    private static Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }

    /**
     * 测试按钮
     */
    private class TestLister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Layer[] layers = mMapView.getLayers();
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
