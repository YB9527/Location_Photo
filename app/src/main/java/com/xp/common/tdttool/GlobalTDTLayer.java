package com.xp.common.tdttool;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

/**
 * * @see 政务外网 (地图初始化)
 * * @author ashmen
 * *
 * 
 */
public class GlobalTDTLayer extends TiledServiceLayer {
  /**
   * 全球天地图地址 例：http://t0.tianditu.gov.cn/vec_c/wmts?LAYER=vec
   * <p>
   * 注：含区别字段，layer名称
   */
          private String wmtsUrl;
  /**
   * 因为是全球天地图需要提供定位到地方天地图显示范围
   */
          private Envelope envelope;
  /**
   * true  使用运维配置范围扩大2个单位
   * false 开发人员获取范围后写入程序中
   * */
          private boolean isDefault=true;

  public GlobalTDTLayer(String wmtsUrl, Envelope envelope) {
    super(true);
    this.wmtsUrl = wmtsUrl;
    if(isDefault){
      this.envelope = new Envelope(envelope.getXMin()-2, envelope.getYMin()-2, envelope.getXMax()+2, envelope.getYMax()+2);
    }else{

//此处为直接固定的地方底图显示范围
      this.envelope=new Envelope(113.71607377092981, 38.0481517905612, 121.45977468312661, 44.74059208753765);
    }
    urlAdjust();

    init();
  }

//处理天地图请求栅格图的拼接地址

  private void urlAdjust() {
    String layerName = wmtsUrl.substring(wmtsUrl.indexOf("m/") + 2, wmtsUrl.indexOf("_"));
    wmtsUrl = wmtsUrl + "?Layer=" + layerName;
  }


  private void init() {
    try {
      getServiceExecutor().submit(new Runnable() {
        @Override
        public void run() {
          GlobalTDTLayer.this.initLayer();
        }
      });
    } catch (RejectedExecutionException rejectedexecutionexception) {
      Log.e("ArcGIS", "initialization of the layer failed.", rejectedexecutionexception);
    }
  }

  @Override
  public byte[] getTile(int level, int col, int row) throws Exception {
    Map<String, String> map = null;
    String resourceURL = null;
    resourceURL = wmtsUrl + "&tk=b23fbe0ee1e8e76533866b75f9d5ed05&SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetTile&STYLE=default&FORMAT=tiles&TILEMATRIXSET=c&TILEMATRIX=" + (level + 1) + "&TILEROW=" + row + "&TILECOL=" + col;
    return com.esri.core.internal.io.handler.a.a(resourceURL, map);
  }

  @Override
  protected void initLayer() {
    if (getID() == 0L) {
      nativeHandle = create();
      changeStatus(com.esri.android.map.event.OnStatusChangedListener.STATUS.fromInt(-1000));
    } else {
      SpatialReference sp = SpatialReference.create(srid);

      Point point = new Point(-180, 90);

      TileInfo tileInfo = new TileInfo(point,
                              SCALES, RESOLUTIONS,
                              SCALES.length, dpi_wmts,
                              tileWidth_wmts, tileHeight_wmts);

      this.setDefaultSpatialReference(sp);
      this.setFullExtent(envelope);
      this.setTileInfo(tileInfo);
      super.initLayer();
    }
  }

  private final int srid = 4490;//3857//4548;
  public final int tileWidth_wmts = 256;
  public final int tileHeight_wmts = 256;
  public final int dpi_wmts = 96;

  public static final double[] SCALES = {
      295497593.0588, 147748796.52937502, 73874398.264687508, 36937199.132343754,
              18468599.566171877, 9234299.7830859385, 4617149.8915429693,
              2308574.9457714846, 1154287.4728857423, 577143.73644287116,
              288571.86822143558, 144285.93411071779, 72142.967055358895,
              36071.483527679447, 18035.741763839724, 9017.8708819198619,
              4508.9354409599309, 2254.4677204799655

          };
  public static final double[] RESOLUTIONS = {
              0.703125000000119, 0.3515625, 0.17578125, 0.087890625,
              0.0439453125, 0.02197265625, 0.010986328125,
              0.0054931640625, 0.00274658203125, 0.001373291015625
              , 0.0006866455078125, 0.00034332275390625,
              0.000171661376953125, 8.58306884765625E-05, 4.291534423828125E-05,
              2.1457672119140625E-05, 1.0728836059570313E-05, 5.3644180297851563E-06
          };
        }

