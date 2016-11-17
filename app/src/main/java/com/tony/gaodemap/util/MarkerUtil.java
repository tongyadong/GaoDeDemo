package com.tony.gaodemap.util;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;

/**
 * Created by gambler on 16/10/14
 */
public class MarkerUtil {
    /**
     * 此类用来处理地图上的标记物
     */

    public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
    public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度
    public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
    public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);// 方恒国际中心经纬度
    public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);// 成都市经纬度
    public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// 西安市经纬度
    public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);// 郑州市经纬度


    /**
     * 在地图上添加marker
     * @param aMap 地图
     * @param latLng 经纬度
     */
    public static void addMarkerToMap(AMap aMap, LatLng latLng) {

       MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(latLng)
                .draggable(true);
        aMap.addMarker(markerOption);
    }

    /**
     * 点击marker时,跳动一下
     * @param aMap 地图
     * @param marker 标记
     */
    public static void jumpPoint(AMap aMap, final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatLng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatLng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatLng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatLng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


    /**
     * 在地图上添加文字信息
     * @param aMap the map that you need to add text
     * @param text  content
     * @param latLng show location
     */
    public static void addTextToMap(AMap aMap, String text, LatLng latLng){
        // 文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色,旋转角度
        TextOptions textOptions = new TextOptions()
                .position(latLng)
                .text(text)
                .fontColor(Color.BLACK)
                .backgroundColor(Color.BLUE)
                .fontSize(30)
                .rotate(20)
                .align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
                .zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);
        aMap.addText(textOptions);
    }
}
