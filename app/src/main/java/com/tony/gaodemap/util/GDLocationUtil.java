package com.tony.gaodemap.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gambler on 16/10/21
 */
public class GDLocationUtil {
    /**
     * 定位相关方法封装
     */

    private static Toast toast;

    public static void showLog(String tag,String content){
        Log.i(tag,content);
    }

    public static void showToast(Context ctx, String content){
        Toast.makeText(ctx, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置定位点显示样式
     * @param aMap 地图
     */
    public static void configLocationPointStyle(AMap aMap) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //定义显示图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        //自定义精度范围的圆形填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
        //设置定位样式
        aMap.setMyLocationStyle(myLocationStyle);
    }

    /**
     * 添加附近搜索监听
     * @param ctx context
     * @param nearbyListener callback about searching result
     */
    public static void addNearbyListener(Context ctx,NearbySearch.NearbyListener nearbyListener){
        NearbySearch.getInstance(ctx).addNearbyListener(nearbyListener);
    }

    /**
     * 单次上传用户信息
     * @param ctx context
     * @param aMapLocation 高德位置
     * @param userId  用户ID
     */
    public static void uploadUserInfoOnce(Context ctx, AMapLocation aMapLocation, String userId){
        /**构造上传位置信息*/
        UploadInfo loadInfo = new UploadInfo();
        /**设置上传位置的坐标系支持AMap坐标数据与GPS数据*/
        loadInfo.setCoordType(NearbySearch.AMAP);
        /**设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取*/
        loadInfo.setPoint(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        /**设置上传用户id*/
        loadInfo.setUserID(userId);
        /**调用异步上传接口*/
        NearbySearch.getInstance(ctx).uploadNearbyInfoAsyn(loadInfo);
    }

    /**
     * 连续上传用户信息
     * @param ctx context
     * @param aMapLocation 高德位置
     * @param userId 用户ID
     * @param interval 上传时间间隔
     */
    public static void UploadUserInfoContinuously(Context ctx, final AMapLocation aMapLocation,
                                                  final String userId,int interval){
        NearbySearch.getInstance(ctx).startUploadNearbyInfoAuto(new UploadInfoCallback() {
            @Override
            public UploadInfo OnUploadInfoCallback() {
                /**构造上传位置信息*/
                UploadInfo loadInfo = new UploadInfo();
                /**设置上传位置的坐标系支持AMap坐标数据与GPS数据*/
                loadInfo.setCoordType(NearbySearch.AMAP);
                /**设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取*/
                loadInfo.setPoint(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                /**设置上传用户id*/
                loadInfo.setUserID(userId);
                return loadInfo;
            }
        }, interval);
    }

    /**
     * 搜索附近
     * @param ctx context
     * @param aMapLocation 高德位置
     */
    public static void startSearchNearbyAsync(Context ctx, AMapLocation aMapLocation, int radius, int time){
        /**设置搜索条件*/
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        /**设置搜索的中心点*/
        query.setCenterPoint(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        /**设置搜索的坐标体系*/
        query.setCoordType(NearbySearch.AMAP);
        /**设置搜索半径*/
        query.setRadius(radius);//默认为3000米，上限为10000米,下限0米
        /**设置查询的时间范围*/
        query.setTimeRange(time);//上限为24小时，下限为5秒，默认为1800秒。
        /**设置查询的方式驾车还是距离*/
        query.setType(NearbySearchFunctionType.DISTANCE_SEARCH);
        /**开启异步查询*/
        NearbySearch.getInstance(ctx).searchNearbyInfoAsyn(query);
    }

    /**
     * 清除用户信息
     * @param ctx context
     */
    public static void clearUserInfo(Context ctx ,String userId){
        NearbySearch.getInstance(ctx).setUserID(userId);
        NearbySearch.getInstance(ctx).clearUserInfoAsyn();
    }

    /**
     * 销毁派单(搜索)对象
     */
    public static void destroyNearbySearch(){
        NearbySearch.destroy();
    }

    /**
     * 在地图上添加marker
     * @param aMap 地图
     * @param latLng 经纬度
     * @param title marker标题
     * @param snippet marker内容
     */
    public static void addMarkerToMap(AMap aMap, LatLng latLng, String title, String snippet) {

        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(latLng)
                .title(title)
                .snippet(snippet)
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

    /**
     * 打印定位结果
     * @param aMapLocation 定位结果
     */
    public static void logAMapLocationInfo(AMapLocation aMapLocation) {
        Log.i("location", "定位类型:" + aMapLocation.getLocationType() + "\n" +
                        "定位精度:" + aMapLocation.getAccuracy() + "\n" +
                        "定位时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date
                        (aMapLocation.getTime())) + "\n" +
                        "纬度:" + aMapLocation.getLatitude() + "\n" +
                        "经度:" + aMapLocation.getLongitude() +
                        "地址:" + aMapLocation.getAddress() + "\n" +
                        "国家:" + aMapLocation.getCountry() + "\n" +
                        "省:" + aMapLocation.getProvince() + "\n" +
                        "城市:" + aMapLocation.getCity() + "\n" +
                        "城区:" + aMapLocation.getDistrict() + "\n" +
                        "街道:" + aMapLocation.getStreet() + "\n" +
                        "街道门牌号:" + aMapLocation.getStreetNum() + "\n" +
                        "城市编码:" + aMapLocation.getCityCode() + "\n"

        );
    }
}
